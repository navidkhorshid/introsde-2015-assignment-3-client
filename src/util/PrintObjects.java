package util;

import ehealth.model.bl.HealthMeasureHistory;
import ehealth.model.bl.HealthProfile;
import ehealth.model.bl.Person;

import java.util.List;

/**
 * Created by Navid on 12/7/2015.
 */
public class PrintObjects {
    void PrintObjects(){}

    public void printPerson(List<Person> personList, boolean print)
    {
        TableBuilder tb = new TableBuilder();
        if(!print || personList.isEmpty()) //if print is False (in case of printing many people), or personlist is empty
        {
            tb.addRow("ID", "FirstName", "LastName", "BirthDate", "MeasureId", "Date", "Time", "Type", "Value", "DataType");
            tb.addRow("--", "---------", "--------", "---------", "---------", "----", "----", "----", "-----", "--------");
        }
        for(Person person : personList) {
            switch (person.getCurrentHealth().getMeasure().size()) {
                case 0: // no healthprofiles
                    if (print) {
                        tb.addRow("ID", "FirstName", "LastName", "BirthDate");
                        tb.addRow("--", "---------", "--------", "---------");
                    }
                    tb.addRow(person.getIdPerson().toString(), person.getFirstname(), person.getLastname(),
                            person.getBirthdate().toString().split("T")[0]);
                    break;
                case 1:
                    if (print) {
                        tb.addRow("ID", "FirstName", "LastName", "BirthDate", "MeasureId", "Date", "Time", "Type", "Value", "DataType");
                        tb.addRow("--", "---------", "--------", "---------", "---------", "----", "----", "----", "-----", "--------");
                    }
                    HealthProfile hp = person.getCurrentHealth().getMeasure().get(0); //get the first
                    tb.addRow(person.getIdPerson().toString(), person.getFirstname(), person.getLastname(),
                            person.getBirthdate().toString().split("T")[0], hp.getMid().toString(),
                            hp.getDateRegistered().toString().split("T")[0],
                            hp.getDateRegistered().toString().split("T")[1],
                            hp.getMeasureValue(), hp.getMeasureType(), hp.getMeasureValueType());
                    break;
                default: //more than two healthprofiles
                    if (print) {
                        tb.addRow("ID", "FirstName", "LastName", "BirthDate", "MeasureId", "Date", "Time", "Type", "Value", "DataType");
                        tb.addRow("--", "---------", "--------", "---------", "---------", "----", "----", "----", "-----", "--------");
                    }
                    HealthProfile hp_first = person.getCurrentHealth().getMeasure().get(0); //get the first
                    tb.addRow(person.getIdPerson().toString(), person.getFirstname(), person.getLastname(),
                            person.getBirthdate().toString().split("T")[0], hp_first.getMid().toString(),
                            hp_first.getDateRegistered().toString().split("T")[0],
                            hp_first.getDateRegistered().toString().split("T")[1],
                            hp_first.getMeasureValue(), hp_first.getMeasureType(), hp_first.getMeasureValueType());

                    for (HealthProfile hp_rest : person.getCurrentHealth().getMeasure()
                            .subList(1, person.getCurrentHealth().getMeasure().size())) {
                        tb.addRow(" ", " ", " ", " ", hp_rest.getMid().toString(),
                                hp_rest.getDateRegistered().toString().split("T")[0],
                                hp_rest.getDateRegistered().toString().split("T")[1],
                                hp_rest.getMeasureValue(), hp_rest.getMeasureType(), hp_rest.getMeasureValueType());
                    }
                    break;
            }
        }
        System.out.println(tb.toString());
    }

    public void printMeasureHistory(List<HealthMeasureHistory> healthMeasureHistoryList)
    {
        TableBuilder tb = new TableBuilder();
        tb.addRow("MeasureId", "Date", "Time", "Type", "Value", "DataType");
        tb.addRow("---------", "----", "----", "----", "-----", "--------");
        for(HealthMeasureHistory history:healthMeasureHistoryList)
        {
            tb.addRow(history.getMid().toString(),history.getDateRegistered().toString().split("T")[0],
                    history.getDateRegistered().toString().split("T")[1], history.getMeasureType(),
                    history.getMeasureValue(),history.getMeasureValueType());
        }
        System.out.println(tb.toString());
    }

    public void printMeasureTypes(List<String> measureTypes)
    {
        TableBuilder tb = new TableBuilder();
        tb.addRow("ID", "Measure Type");
        tb.addRow("--", "------------");
        int i = 1;
        for(String tmp:measureTypes)
        {
            tb.addRow((i++)+"",tmp);
        }
        System.out.println(tb.toString());
    }
}
