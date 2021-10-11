package org.mockito_inside_prod;

import java.util.ArrayList;
import java.util.List;


public interface VeryDifficultToCreateService {

    int getInt();
    int getInt( int i );

    Object getObject();
    Object getObject( Object o );
    Object getObject( Object p1, Object p2, Object p3 );

    String getString();

    List<Object> getList();
    ArrayList<Object> getArrayList();

    VeryComplexObject getVeryComplexObject();
}
