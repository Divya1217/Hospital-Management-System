package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagementSystem {

	private static final String url = "jdbc:mysql://localhost:3306/hospital";
	private static final String username = "root";
	private static final String password = "root";
 
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
			
		}
		Scanner scanner =new Scanner(System.in);
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			Patients patient = new Patients(connection, scanner);
			Doctors doctor = new Doctors(connection);
			while(true) {
				System.out.println("HOSPITAL MANAGEMENT SYSTEM");
				System.out.println("1. Add Patient"); 
				System.out.println("2. View Patients");
				System.out.println("3. View Doctors");
				System.out.println("4. Book Appointment");
				System.out.println("5. Exit");
                System.out.println("enter your choice: ");
                int choice = scanner.nextInt();
                
                switch(choice) {
                case 1: 
                	patient.addPatient();
                	System.out.println();
                case 2: 
                	patient.viewPatient();
                	System.out.println();
                case 3:
                	doctor.viewDoctor();
                	System.out.println();
                case 4:
                	bookAppointmet(patient, doctor, connection, scanner);
                	System.out.println();
                case 5: 
                	return;
                default:
                	System.out.println("enter valid choice..");
                	
                }
                
 

			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	public static void bookAppointmet(Patients patients, Doctors doctor ,Connection connection, Scanner scanner) {
		System.out.println("enter patient id: ");
		int patientId = scanner.nextInt();
		System.out.println("enter doc id: ");
		int doctorId = scanner.nextInt();
		System.out.println("enter appoinment date(YYYY-MM-DD): ");
		String appointmentDate = scanner.next();
		if(patients.getPatientsById(patientId) && doctor.getDoctorsById(doctorId)) {
			if(checkDoctorAvailability(doctorId, appointmentDate,connection)) {
				String appointmentQuery = "INSERT INTO appointments(patients_id, doctors_id, appointmennt_date) VALUES(?,?,?)" ;
				try{PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
						preparedStatement.setInt(1,patientId);
				        preparedStatement.setInt(2,doctorId);
				        preparedStatement.setString(3,appointmentDate);
				        
				        int rowsAffected = preparedStatement.executeUpdate();
				        if(rowsAffected>0) {
				        	System.out.println("Appointment booked..");
				        }
				        else {
				        	
				        	System.out.println("failed to book appointment");
				        }
				}
				catch(SQLException e){
					e.printStackTrace();
				}
				
			}
			else {
				System.out.println("Doctor not available on this date..");
			}
		}
		else {
			System.out.println("either doctor or patient doesn't exist!!!");
		}
	}
	
	public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection) {
		String query = "SELECT COUNT(*) from appointments WHERE doctor_Id = ? AND appointment_date = ?";
	    try {
	    	PreparedStatement preparedStatement = connection.prepareStatement(query);
	    	
	        preparedStatement.setInt(1,doctorId);
	        preparedStatement.setString(2,appointmentDate);
	    	ResultSet resultSet = preparedStatement.executeQuery();
	    	if(resultSet.next()) {
	    		int count = resultSet.getInt(1);
	    		if(count==0) {
	    			return true;
	    		
	    		}
	    		else {
	    			return false;
	    		}
	    	}
	    }
	   
	    catch(SQLException e) {
	    	e.printStackTrace();
	    	
	    }
	    
	
	return false;
	
	}
	
}






