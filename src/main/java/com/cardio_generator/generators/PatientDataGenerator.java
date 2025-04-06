package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Interface for generating patient health data.
 * Implementations should provide specific data generation logic.
 */

public interface PatientDataGenerator {
    void generate(int patientId, OutputStrategy outputStrategy);
}
