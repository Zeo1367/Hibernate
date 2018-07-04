package com.zeo.contactmgr;

import com.zeo.contactmgr.model.Contact;
import com.zeo.contactmgr.model.Contact.ContactBuilder;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class Application {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static int save(Contact contact){

//        Open a session
        Session session = sessionFactory.openSession();
        //start transaction
        session.beginTransaction();
        //save the object into our database *java persistence on its way*
        int id= (int)session.save(contact);
        //commit the transaction
        session.getTransaction().commit();
        //close the session
        session.close();
        return id;
    }

    private static void update(Contact contact){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(contact);
        session.getTransaction().commit();
        session.close();
    }

    @SuppressWarnings("unchecked")
    private static List<Contact> fetchAllContacts(){
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Contact.class);
        List<Contact> contacts = criteria.list();
        session.close();
        return contacts;
    }

    private static Contact fetchContactById(int id){
        Session session = sessionFactory.openSession();
        Contact contact = session.get(Contact.class,id);
        session.close();
        return contact;
    }

    public static void main(String[] args) {
//        Contact contact = new Contact.ContactBuilder("Aditya","Chand")
//                          .withEmail("adityachnd78@gmail.com")
//                          .withPhone(8527948567L).build();
        Contact contact = new ContactBuilder("Aditya","Chand")
                .withEmail("adityachnd78@gmail.com")
                .withPhone(8527948567L).build();
        // System.out.println(contact);
        int id=save(contact);
//        for(Contact contact1:fetchAllContacts()){
//            System.out.println(contact);
//        }
        System.out.println("Before update");
        fetchAllContacts().stream().forEach(System.out::println);
        Contact contact1=fetchContactById(id);
        contact1.setFirstName("Adi");
        System.out.println("Updating....");
        update(contact1);
        System.out.println("After update...");
        fetchAllContacts().stream().forEach(System.out::println);
    }
}
