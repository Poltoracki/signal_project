package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * AlertGenerator is responsible for generating alert data for patients.
 * It simulates the triggering and resolution of alerts based on a random probability.
 * The alert state is maintained for each patient, and the output is sent to the specified output strategy.
 *
 * <p>Alert states are represented as boolean values:
 * <ul>
 *     <li>false = resolved</li>
 *     <li>true = pressed</li>
 * </ul>
 * 
 * Output data is sent via the configured {@link OutputStrategy}.
 */
public class AlertGenerator implements PatientDataGenerator {
    /**
     * A shared random number generator used for probabilistic alert simulation.
     */
    public static final Random randomGenerator = new Random();
    /**
     * The array of alert states for each patient.
     * The index corresponds to the patient ID.
     */
    private boolean[] AlertStates; // false = resolved, true = pressed
    /**
     * Constructs an AlertGenerator with a specified number of patients.
     *
     * @param patientCount The number of patients to monitor for alerts.
     */
    public AlertGenerator(int patientCount) {
        AlertStates = new boolean[patientCount + 1];
    }
    /**
     * Generates alert data for a specific patient.
     * The alert state is determined based on a random probability.
     *
     * @param patientId The ID of the patient.
     * @param outputStrategy The output strategy to use for sending the generated data.
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (AlertStates[patientId]) {
                if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
                    AlertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                double Lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                double p = -Math.expm1(-Lambda); // Probability of at least one alert in the period
                boolean alertTriggered = randomGenerator.nextDouble() < p;

                if (alertTriggered) {
                    AlertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}