package arcs.commands.staff;

import arcs.commands.Command;
import arcs.commands.CommandResult;
import arcs.data.staff.Staff;
import arcs.data.exception.DuplicateDataException;

import java.util.ArrayList;

public class AddStaffCommand extends Command  {
    public static final String COMMAND_WORD = "addstaff";
    private Staff toAdd;
    private ArrayList<String> emptyFields = new ArrayList<>();
    private ArrayList<String> invalidFields = new ArrayList<>();

    private static final String SUCCESS_MESSAGE = "OK! The following new staff is added: ";
    private static final String DUPLICATE_MESSAGE = "The Id number already exits. This staff cannot be added.";
    private static final String EMPTY_FIELD_MESSAGE = "These necessary fields are not specified:";
    private static final String INVALID_FIELD_MESSAGE = "These fields are invalid:";

    public AddStaffCommand(String Id, String password, String name, String job, String phone, String email) {
        checkEmptyField(Id, password,name, job,phone, email);
        if (emptyFields.isEmpty()) {
            checkInvalidField(Id, phone, email);
        }
        if (emptyFields.isEmpty() && invalidFields.isEmpty()) {
            toAdd = new Staff(Id,  password,name, job,phone, email);
        }
    }

    @Override
    public CommandResult execute() {
        if (!emptyFields.isEmpty()) {
            return new CommandResult(EMPTY_FIELD_MESSAGE, emptyFields);
        }
        if (!invalidFields.isEmpty()) {
            return new CommandResult(INVALID_FIELD_MESSAGE, invalidFields);
        }

        try {
            staffManager.addStaff(toAdd);
            return new CommandResult(SUCCESS_MESSAGE
                    + System.lineSeparator() + toAdd.getStaffInfo());
        } catch (DuplicateDataException e) {
            return new CommandResult(DUPLICATE_MESSAGE);
        }
    }

    public void checkEmptyField(String Id, String password, String name, String job, String phone, String email) {

        if (Id == null || Id.isEmpty()) {
            emptyFields.add("Id");
        }
        if (name == null || name.isEmpty()) {
            emptyFields.add("Name");
        }
        if (phone == null || phone.isEmpty()) {
            emptyFields.add("Phone number");
        }
        if (email == null || email.isEmpty()) {
            emptyFields.add("Email");
        }
        if (password == null || email.isEmpty()) {
            emptyFields.add("Password");
        }
        if (job == null || email.isEmpty()) {
            emptyFields.add("Job");
        }
    }

    public void checkInvalidField(String Id, String phone, String email) {
        assert Id != null && phone != null && email != null : "staff field is null.";
        if (!Staff.isValidId(Id)) {
            invalidFields.add("Id");
        }
        if (!Staff.isValidPhone(phone)) {
            invalidFields.add("Phone number");
        }
        if (!Staff.isValidEmail(email)) {
            invalidFields.add("Email");
        }
    }
}
