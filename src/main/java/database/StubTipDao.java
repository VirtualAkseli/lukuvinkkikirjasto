package tietokanta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vinkkilogic.Tip;

public class StubTipDao implements Dao<Tip, Long> {

    @Override
    public Tip get(Long key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void update(Tip object) {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete(Long key) {
        // TODO Auto-generated method stub

    }

    @Override
    public Long create(Tip object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Tip> getByValue(Map<String, Object> map) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteByValue(Map<String, Object> map) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Tip> list() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
