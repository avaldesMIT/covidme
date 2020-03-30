package me.covid.cov_idme.model;

/**
 * Represents a positive infection result the user received and is being submitted so that we can advise
 * other users
 */
public class ResultSubmission {

    private final boolean byDoctor;

    private final boolean byTest;

    /**
     * Creates a new instance of this
     *
     * @param byDoctor - whether the user was diagnosed by a physician
     * @param byTest - whether the user was diagnosed as a result of a positive test result for SARS-Cov-2
     */
    public ResultSubmission(boolean byDoctor, boolean byTest) {
        this.byDoctor = byDoctor;
        this.byTest = byTest;
    }

    /**
     * Determines whether or not the user was diagnosed by a physician
     *
     * @return true if and only if the user was diagnosed by a physician
     */
    public boolean isByDoctor() {
        return byDoctor;
    }

    /**
     * Determines whether or not the user was diagnosed as a result of a positive test result for SARS-Cov-2
     *
     * @return true if and only if the user was diagnosed as a result of a positive test result for SARS-Cov-2
     */
    public boolean isByTest() {
        return byDoctor;
    }

    @Override
    public String toString() {
        return "ResultSubmission{" +
                "byDoctor=" + byDoctor +
                ", byTest=" + byTest +
                '}';
    }
}
