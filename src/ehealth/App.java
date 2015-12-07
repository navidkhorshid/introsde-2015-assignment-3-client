package ehealth;

import ehealth.model.bl.HealthMeasureHistory;
import ehealth.model.bl.HealthProfile;
import ehealth.model.bl.PeopleBL;
import ehealth.model.bl.Person;
import util.PrintObjects;
import util.TeePrintStream;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class App
{
    public static void main(String[] args) throws IllegalArgumentException, IOException, URISyntaxException
    {
        URL url = new URL("http://quiet-eyrie-9945.herokuapp.com/ws/people?wsdl");
        //1st argument service URI, refer to wsdl document above
        //2nd argument is service name, refer to wsdl document above1
        QName qname = new QName("http://bl.model.ehealth/", "PeopleService");
        Service service = Service.create(url, qname);
        PeopleBL people = service.getPort(PeopleBL.class);

        //print to Log file
        writeToLog();

        //printing wsdl url
        System.out.println("WSDL Url: ");
        System.out.println(url);

        printLine();

        //public List<Person> readPersonList();
        System.out.println("Method #1: readPersonList(): ");
        List<Person> peopleList = people.readPersonList();
        new PrintObjects().printPerson(peopleList, false);

        printLine();

        //public Person readPerson(@WebParam(name="idPerson") Long id);
        System.out.println("Method #2: readPerson(Long id = 1): ");
        // read person with id 1
        Person p = people.readPerson(Long.valueOf(1));
        //print person
        peopleList.clear();
        if(p.getIdPerson()!=null)
            peopleList.add(p);
        new PrintObjects().printPerson(peopleList, true);

        printLine();

        //public void updatePerson(@WebParam(name="person") Person person);
        System.out.println("Method #3: updatePerson(Person p): Person with ID = 1");
        // change name of the person with id 1
        String uuid = UUID.randomUUID().toString();
        p.setFirstname("Bot_" + uuid.substring(0, 8));
        people.updatePerson(p);
        System.out.println("Method #2: readPerson(Long id = 1): ");
        // read person with id 1
        p = people.readPerson(Long.valueOf(1));
        //Print person
        peopleList.clear();
        if(p.getIdPerson()!=null)
            peopleList.add(p);
        new PrintObjects().printPerson(peopleList, true);

        printLine();

        //public Person createPerson(@WebParam(name="person") Person person);
        System.out.println("Method #4: createPerson(Person p): ");
        Person.CurrentHealth currentHealth = new Person.CurrentHealth();
        //First healthprofile
        HealthProfile healthProfile = new HealthProfile();
        Calendar calendar = Calendar.getInstance();
        try
        {
            healthProfile.setDateRegistered(DatatypeFactory.newInstance().newXMLGregorianCalendar(
                    new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND))));

        }catch (Exception e){}
        Random method4_rand = new Random();
        int method4_value = method4_rand.nextInt(30) + 60;
        healthProfile.setMeasureType("weight");
        healthProfile.setMeasureValue(method4_value+"");// a number between 60-90
        currentHealth.getMeasure().add(healthProfile);
        //second healthprofile
        HealthProfile healthProfile1 = new HealthProfile();
        try
        {
            healthProfile1.setDateRegistered(DatatypeFactory.newInstance().newXMLGregorianCalendar(
                    new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND))));

        }catch (Exception e){}
        healthProfile1.setMeasureType("height");
        int method4_value_2 = method4_rand.nextInt(30) + 160;
        healthProfile1.setMeasureValue(method4_value_2 + "");
        currentHealth.getMeasure().add(healthProfile1);
        //send to method
        int method4_name = method4_rand.nextInt(100) + 1;
        p = people.createPerson(createPersonObject("Navid"+method4_name, "Shamsizadeh"+method4_name,
                new GregorianCalendar(1990, 2, 3), currentHealth));
        System.out.println("Newly created Person: ");
        //print new person
        peopleList.clear();
        if(p.getIdPerson()!=null)
            peopleList.add(p);
        new PrintObjects().printPerson(peopleList, true);

        printLine();

        //public void deletePerson(@WebParam(name="person") Person person);
        System.out.println("Method #5: deletePerson(Long id = "+p.getIdPerson()+"): ");
        //Delete the created person
        people.deletePerson(p);
        System.out.println("Method #1: readPersonList(): ");
        //print all the people again
        new PrintObjects().printPerson(people.readPersonList(), false);

        printLine();

        //public List<HealthMeasureHistory> readPersonHistory(@WebParam(name="idPerson") Long idPerson,@WebParam(name="measureType") String measureType);
        System.out.println("Method #6: readPersonHistory(Long id = 3, String measureType = \"weight\"): ");
        List<HealthMeasureHistory> healthMeasureHistoryList = people.readPersonHistory(Long.valueOf(3), "weight");
        new PrintObjects().printMeasureHistory(healthMeasureHistoryList);

        printLine();

        // public List<String> readMeasureTypes();
        System.out.println("Method #7: readMeasureTypes(): ");
        List<String> measureTypes = people.readMeasureTypes();
        new PrintObjects().printMeasureTypes(measureTypes);

        printLine();

        //public HealthMeasureHistory readPersonMeasure(@WebParam(name="idPerson") Long idPerson,@WebParam(name="measureType") String measureType,@WebParam(name="mid") Long mid);
        System.out.println("Method #8: readPersonMeasure(Long id = 3, String measureType = \"weight\", Long mid = 7): ");
        HealthMeasureHistory history = people.readPersonMeasure(Long.valueOf(3), "weight", Long.valueOf(7));
        healthMeasureHistoryList.clear();
        if(history.getMid()!=null)
            healthMeasureHistoryList.add(history);
        new PrintObjects().printMeasureHistory(healthMeasureHistoryList);

        printLine();

        //public void savePersonMeasure(@WebParam(name="idPerson") Long id,@WebParam(name="measure") HealthMeasureHistory healthMeasureHistory);
        System.out.println("Method #9: savePersonMeasure(Long id = 3, Measure m = %random_measure%): ");
        HealthMeasureHistory measure = new HealthMeasureHistory();
        Random method9_rand = new Random();
        int method9_year = method9_rand.nextInt(5) + 2010;
        int method9_month = method9_rand.nextInt(11) + 1;
        int method9_day = method9_rand.nextInt(26) + 1;
        int method9_hour = method9_rand.nextInt(22) + 1;
        int method9_min = method9_rand.nextInt(58) + 1;
        int method9_sec = method9_rand.nextInt(58) + 1;
        try
        {
            measure.setDateRegistered(DatatypeFactory.newInstance().newXMLGregorianCalendar(
                    new GregorianCalendar(method9_year, method9_month, method9_day, method9_hour, method9_min, method9_sec)));
        } catch (Exception e){}
        measure.setMeasureType("weight");
        int method9_value = method9_rand.nextInt(30) + 60;
        measure.setMeasureValue(method9_value + "");
        //you should use this for method 10 because it has Mid
        HealthMeasureHistory method10_MeasureHistory = people.savePersonMeasure(Long.valueOf(3), measure);
        System.out.println("Method #6: readPersonHistory(Long id = 3, String measureType = \"weight\"): ");
        //read measure history
        healthMeasureHistoryList = people.readPersonHistory(Long.valueOf(3), "weight");
        new PrintObjects().printMeasureHistory(healthMeasureHistoryList);

        printLine();

        //public HealthMeasureHistory updatePersonMeasure(@WebParam(name="idPerson") Long id,@WebParam(name="measure") HealthMeasureHistory healthMeasureHistory);
        System.out.println("Method #10: updatePersonMeasure(Long id = 3, Measure m = %Measure with Mid = "+method10_MeasureHistory.getMid()+" AND Random Value, Date%): ");
        Random method10_rand = new Random();
        int method10_value = method10_rand.nextInt(30) + 60;
        method10_MeasureHistory.setMeasureValue(method10_value + "");
        int method10_year = method10_rand.nextInt(5) + 2010;
        int method10_month = method10_rand.nextInt(11) + 1;
        int method10_day = method10_rand.nextInt(26) + 1;
        int method10_hour = method10_rand.nextInt(22) + 1;
        int method10_min = method10_rand.nextInt(58) + 1;
        int method10_sec = method10_rand.nextInt(58) + 1;
        try
        {
            method10_MeasureHistory.setDateRegistered(DatatypeFactory.newInstance().newXMLGregorianCalendar(
                    new GregorianCalendar(method10_year, method10_month, method10_day, method10_hour, method10_min, method10_sec)));
        }catch (Exception e){}
        method10_MeasureHistory = people.updatePersonMeasure(Long.valueOf(3),method10_MeasureHistory);
        System.out.println("Method #6: readPersonHistory(Long id, String measureType): ");
        //read measure history
        healthMeasureHistoryList = people.readPersonHistory(Long.valueOf(3), "weight");
        new PrintObjects().printMeasureHistory(healthMeasureHistoryList);

        printLine();
    }

    public static Person createPersonObject(String firstName, String lastName, GregorianCalendar birthDate, Person.CurrentHealth measure)
    {
        Person person = new Person();
        person.setIdPerson(Long.valueOf(0));
        person.setFirstname(firstName);
        person.setLastname(lastName);
        try
        {
            person.setBirthdate(DatatypeFactory.newInstance().newXMLGregorianCalendar(birthDate));
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        person.setCurrentHealth(measure);

        return person;
    }

    //Writing to LOG file
    public static void writeToLog(){
        //Printing in Log file as well as console out
        try
        {
            FileOutputStream file = new FileOutputStream("client-server.log");
            TeePrintStream tee = new TeePrintStream(file, System.out);
            System.setOut(tee);
        }catch (Exception e){}
    }

    public static void printLine()
    {
        String tmp = "#";
        for(int i=0;i<40;i++)
            tmp+="###";
        System.out.println("\n"+tmp+"\n");
    }

}
