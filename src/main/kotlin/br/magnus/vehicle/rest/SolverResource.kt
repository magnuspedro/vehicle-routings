package br.magnus.vehicle.rest

import br.magnus.vehicle.domain.converter.ConvertToSolution
import br.magnus.vehicle.domain.entities.Customer
import br.magnus.vehicle.domain.entities.Depot
import br.magnus.vehicle.domain.entities.Vehicle
import br.magnus.vehicle.domain.entities.VehicleRoutingSolution
import br.magnus.vehicle.persistence.VehicleRoutingSolutionRepository
import br.magnus.vehicle.persistence.converter.ConvertFromDb
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.optaplanner.core.api.score.ScoreManager
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore
import org.optaplanner.core.api.solver.SolverManager
import org.springframework.web.bind.annotation.*
import java.util.concurrent.atomic.AtomicReference

@RestController
@RequestMapping("/vrp")
class SolverResource(
    private val repository: VehicleRoutingSolutionRepository,
    private val solverManager: SolverManager<VehicleRoutingSolution, Long>,
    private val scoreManager: ScoreManager<VehicleRoutingSolution, HardSoftLongScore>,
    private val convertFromDb: ConvertFromDb,
    private val convertToSolution: ConvertToSolution

) {
    companion object {
        const val PROBLEM_ID: Long = 0L
    }

    private val solverError: AtomicReference<Throwable> = AtomicReference()
    private val mapper: ObjectMapper = jacksonObjectMapper()


    private fun statusFromSolution(solution: VehicleRoutingSolution): Status {
        return Status(
            solution, scoreManager.explainScore(solution)?.summary, solverManager.getSolverStatus(PROBLEM_ID)
        )
    }

    @GetMapping("status")
    fun status(): Status {
        solverError.getAndSet(null)?.also { throwable ->
            throw RuntimeException("Solver failed", throwable)
        }

        val solution = repository.solution() ?: VehicleRoutingSolution.empty()
        return statusFromSolution(solution)
    }

    @PostMapping("solve")
    fun solve() {
        repository.solution().also { vehicleRoutingSolution ->
            solverManager.solveAndListen(
                PROBLEM_ID,
                { vehicleRoutingSolution },
                { solution -> repository.update(solution) },
                { _: Long, throwable -> solverError.set(throwable) })
        }
    }

    @PostMapping("stopSolving")
    fun stopSolving() {
        solverManager.terminateEarly(PROBLEM_ID)
    }

    @GetMapping("list")
    fun list(): Triple<List<Customer>, List<Depot>, List<Vehicle>> {
        return convertFromDb.convert()
    }

    @PostMapping("create")
    fun create(@RequestBody message: Triple<List<Customer>, List<Depot>, List<Vehicle>>) {
        convertToSolution.convertAndSovle(message)
    }

}