package com.task.model;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

public class XmlMarshaller<T> {
	private JAXBContext jc;
	public XmlMarshaller(String classToBeBound) {
		try {
			this.jc = JAXBContext.newInstance(classToBeBound);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	} 
	@SuppressWarnings("unchecked")
	public T unmarshall(String inputXml) {
		Unmarshaller u = null;

		try {
			u = jc.createUnmarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		T result = null;
		try {
			result = (T) u.unmarshal(new File(inputXml));
		} 
		catch (JAXBException e) {
			e.printStackTrace();
		} 
		return result;
	}
	
public void marshal(T toSerialize,String outputXml) {
	Marshaller m = null;
	try {
		m = jc.createMarshaller();
	} catch (JAXBException e) {
		e.printStackTrace();
	}
	try {
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	} catch (PropertyException e) {
		e.printStackTrace();
	}
	try {
		File file=new File(outputXml);
		if(!file.exists()){
			file.createNewFile();
		}
		m.marshal(toSerialize, file);
	} catch (JAXBException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
}
}
