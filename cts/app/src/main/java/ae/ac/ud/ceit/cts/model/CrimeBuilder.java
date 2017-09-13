package ae.ac.ud.ceit.cts.model;

/**
 * Created by atalla on 09/04/17.
 */

public interface CrimeBuilder {

    CrimeBuilder setReference(final String reference);
    CrimeBuilder setDate_time(final String date_time) ;
    CrimeBuilder setCreated(final String created);
    CrimeBuilder setEvidences(final String evidences) ;
    CrimeBuilder setLocation(final String location);
    CrimeBuilder setSuspects(final String suspects) ;
    CrimeBuilder setVictims(final String victims);


    Crime build();
}