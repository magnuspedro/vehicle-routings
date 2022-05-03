package br.magnus.vehicle.rest

import br.magnus.vehicle.domain.entities.VehicleRoutingSolution
import org.optaplanner.core.api.solver.SolverStatus

class Status(
    val solution: VehicleRoutingSolution,
    val scoreExplanation: String?,
    private val solverStatus: SolverStatus?
) {
    val isSolving: Boolean = solverStatus != SolverStatus.NOT_SOLVING
}
