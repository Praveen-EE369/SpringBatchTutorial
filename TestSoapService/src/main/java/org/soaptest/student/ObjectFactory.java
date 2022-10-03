//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.10.02 at 10:46:52 PM IST 
//


package org.soaptest.student;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.soaptest.student package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.soaptest.student
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UpdateStudentRequest }
     * 
     */
    public UpdateStudentRequest createUpdateStudentRequest() {
        return new UpdateStudentRequest();
    }

    /**
     * Create an instance of {@link AddStudentRequest }
     * 
     */
    public AddStudentRequest createAddStudentRequest() {
        return new AddStudentRequest();
    }

    /**
     * Create an instance of {@link UpdateStudentResponse }
     * 
     */
    public UpdateStudentResponse createUpdateStudentResponse() {
        return new UpdateStudentResponse();
    }

    /**
     * Create an instance of {@link CallStatus }
     * 
     */
    public CallStatus createCallStatus() {
        return new CallStatus();
    }

    /**
     * Create an instance of {@link GetStudentResponse }
     * 
     */
    public GetStudentResponse createGetStudentResponse() {
        return new GetStudentResponse();
    }

    /**
     * Create an instance of {@link AddStudentResponse }
     * 
     */
    public AddStudentResponse createAddStudentResponse() {
        return new AddStudentResponse();
    }

    /**
     * Create an instance of {@link GetStudentByIdRequest }
     * 
     */
    public GetStudentByIdRequest createGetStudentByIdRequest() {
        return new GetStudentByIdRequest();
    }

    /**
     * Create an instance of {@link DeleteStudentRequest }
     * 
     */
    public DeleteStudentRequest createDeleteStudentRequest() {
        return new DeleteStudentRequest();
    }

    /**
     * Create an instance of {@link Student }
     * 
     */
    public Student createStudent() {
        return new Student();
    }

    /**
     * Create an instance of {@link DeleteStudentResponse }
     * 
     */
    public DeleteStudentResponse createDeleteStudentResponse() {
        return new DeleteStudentResponse();
    }

}