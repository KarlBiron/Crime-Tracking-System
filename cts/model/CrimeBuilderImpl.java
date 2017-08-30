package ae.ac.ud.ceit.cts.model;



/**
 * Created by atalla on 09/04/17.
 */

public class CrimeBuilderImpl implements CrimeBuilder {

    private Crime crime;

    public CrimeBuilderImpl() {
        crime = new Crime();
    }



    @Override
    public CrimeBuilder setReference(String reference) {
        crime.setReference(reference);
        return this;
    }

    @Override
    public CrimeBuilder setDate_time(String date_time) {
        crime.setDate_time(date_time);
        return this;
    }

    @Override
    public CrimeBuilder setCreated(String created) {
        crime.setCreated(created);
        return this;
    }

    @Override
    public CrimeBuilder setEvidences(String evidences) {
        crime.setEvidences(evidences);
        return this;
    }

    @Override
    public CrimeBuilder setLocation(String location) {
        crime.setLocation(location);
        return this;
    }

    @Override
    public CrimeBuilder setSuspects(String suspects) {
        crime.setSuspects(suspects);
        return this;
    }

    @Override
    public CrimeBuilder setVictims(String victims) {
        crime.setVictims(victims);
        return this;
    }

    @Override
    public Crime build() {
        return crime;
    }
}