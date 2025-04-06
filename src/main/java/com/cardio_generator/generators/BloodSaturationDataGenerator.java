package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates simulated blood saturation data for patients by implementing the PatientDataGenerator interface.
 * Blood saturation values are maintained within a realistic range (90-100%) with small random fluctuations.
 */
public class BloodSaturationDataGenerator implements PatientDataGenerator {

    private static final Random random = new Random();
    private int[] lastSaturationValues;

    /**
     * Constructs a new BloodSaturationDataGenerator for a specified number of patients.
     * Each patient's initial saturation value is randomly set between 95% and 100%.
     *
     * @param patientCount The total number of patients for which data will be generated.
     */
    public BloodSaturationDataGenerator(int patientCount) {
        lastSaturationValues = new int[patientCount + 1];

        // Initialize with baseline saturation values for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + random.nextInt(6); // Initializes with a value between 95 and 100
        }
    }

    /**
     * Generates and outputs a new blood saturation data point for a specific patient.
     * Simulates small fluctuations to maintain realism within a healthy range.
     *
     * @param patientId      The unique identifier of the patient.
     * @param outputStrategy The strategy to output the generated saturation data.
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Simulate blood saturation values
            int variation = random.nextInt(3) - 1; // -1, 0, or 1 to simulate small fluctuations
            int newSaturationValue = lastSaturationValues[patientId] + variation;

            // Ensure the saturation stays within a realistic and healthy range
            newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
            lastSaturationValues[patientId] = newSaturationValue;
            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation",
                    Double.toString(newSaturationValue) + "%");
        } catch (Exception e) {
            System.err.println("An error occurred while generating blood saturation data for patient " + patientId);
            e.printStackTrace(); // Prints the stack trace to help identify the error source.
        }
    }
}
