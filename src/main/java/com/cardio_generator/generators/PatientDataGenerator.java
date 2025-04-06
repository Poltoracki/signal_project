package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Interface for generating patient health data.
 * Implementations should provide specific data generation logic.
 */

public interface PatientDataGenerator {
    /**
     * Generates health data for a specific patient.
     *
     * @param patientId The ID of the patient.
     * @param outputStrategy The output strategy to use for sending the generated data.
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
