package database;

import controllers.MainController;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import sun.rmi.runtime.Log;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateEntity {
    private static final Logger LOG = Logger.getLogger(CreateEntity.class.getName());

    public static Company getCompanyFromID(Integer companyID) {
        Session session = CreateDatabase.getSession();
        session.beginTransaction();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Company> cr = cb.createQuery(Company.class);
        Root<Company> root = cr.from(Company.class);

        cr.select(root).where(cb.gt(root.get("id"), companyID - 1));

        Query<Company> query = session.createQuery(cr);
        query.setMaxResults(1);
        List<Company> results = query.getResultList();
        session.getTransaction().commit();
        session.close();

        if (results.isEmpty()) {
            LOG.log(Level.WARNING, "Take cislo miestnosti neexistuje");
            return new Company();
        }

        return results.get(0);

    }

    public static void createPerson(String firstName, String lastName, String username,
                                    String password, String mail, String phoneNumber, int company) {
        try {
            Session session = CreateDatabase.getSession();

            Transaction t = session.beginTransaction();

            session.save(new Person(firstName, lastName, username, password, mail, phoneNumber, getCompanyFromID(company)));

            t.commit();
            session.close();

            System.out.println("Successfully saved new person...");
        } catch(Exception e) {
            LOG.log(Level.SEVERE, "Nepodarilo sa pridat zaznam");
        }
    }
    public static void createCompany(String name, String street, String city,
                                     String country, String postalCode, String mail, String phoneNumber) {

        Session session = CreateDatabase.getSession();
        Transaction t = session.beginTransaction();

        session.save(new Company(name, street, city, country, postalCode, mail, phoneNumber));

        t.commit();

        session.close();
        System.out.println("Successfully saved new company...");
    }
}