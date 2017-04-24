package com.mebitech.robe.convert.tsv;

import com.mebitech.robe.convert.csv.CSVImporter;
import org.supercsv.prefs.CsvPreference;

public class TSVImporter<T> extends CSVImporter<T> {

    public TSVImporter(Class dataClass) {
        super(dataClass, CsvPreference.TAB_PREFERENCE);
    }

}
