import {
    Formik,
    Form,
    useField,
} from 'formik';
import * as Yup from 'yup';
import {
    Alert,
    AlertIcon,
    Box,
    Button,
    FormLabel,
    Input,
    Select,
    Stack,
} from "@chakra-ui/react";
import {
    saveCustomer,
    updateCustomer,
} from "../services/client.js";
import {
    errorNotification,
    successNotification,
} from "../services/notification.js";

const MyTextInput = ({ label, ...props }) => {
    // useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
    // which we can spread on <input>. We can use field meta to show an error
    // message if the field is invalid and it has been touched (i.e. visited)
    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Input className="text-input" {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon/>
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};

const MySelect = ({ label, ...props }) => {
    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Select {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon/>
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};

const MyBoth = () => {
    return (
        <>
            <MyTextInput
                label="Name"
                name="name"
                type="text"
                placeholder="Name"
            />

            <MyTextInput
                label="Email"
                name="email"
                type="email"
                placeholder="email@gmail.com"
            />

            <MyTextInput
                label="Age"
                name="age"
                type="number"
                placeholder="20"
            />

            <MySelect label="Gender" name="gender">
                <option value="">Select gender</option>
                <option value="Male">Male</option>
                <option value="Female">Female</option>
            </MySelect>
        </>
    );
}

const CustomerForm = ({
    fetchCustomers,
    formType,
    id,
    initialValues,
    onClose,
}) => {

    return (
        formType === "create" ? (
            <>
                <Formik
                    initialValues={{
                        name: '',
                        email: '',
                        age: '',
                        gender: '',
                    }}
                    validationSchema={Yup.object({
                        name: Yup.string()
                            .max(15, 'Must be 15 characters or less')
                            .required('Name Required'),
                        email: Yup.string()
                            .email('Invalid email address')
                            .required('Email Required'),
                        age: Yup.number()
                            .min(16, 'Must be at least 16 years or older')
                            .max(100, 'Must be less than 100 years or older')
                            .required('Age Required'),
                        gender: Yup.string()
                            .oneOf(
                                ['Male', 'Female'],
                                'Invalid Gender'
                            )
                            .required('Gender Required'),
                    })}
                    onSubmit={(customer, { setSubmitting }) => {
                        setSubmitting(true);
                        saveCustomer(customer)
                            .then(res => {
                                successNotification(
                                    "Customer Saved",
                                    `${customer.name} was created successfully`
                                );
                                fetchCustomers();
                                onClose();
                            }).catch(err => {
                                errorNotification(
                                    err.code,
                                    err.response.data.message
                                );
                            }).finally(() => {
                                setSubmitting(false);
                            });
                    }}
                >
                    {({isValid, isSubmitting}) => {
                        return (
                            <Form>
                                <Stack spacing={"24px"}>
                                    <MyBoth/>
                                    <Button isDisabled={!isValid || isSubmitting} type="submit">Submit</Button>
                                </Stack>
                            </Form>
                        );
                    }}
                </Formik>
            </>
        ) : (
            <>
                <Formik
                    initialValues={initialValues}
                    validationSchema={Yup.object({
                        name: Yup.string()
                            .max(15, 'Must be 15 characters or less'),
                        email: Yup.string()
                            .email('Invalid email address'),
                        age: Yup.number()
                            .min(16, 'Must be at least 16 years or older')
                            .max(100, 'Must be less than 100 years or older'),
                        gender: Yup.string()
                            .oneOf(
                                ['Male', 'Female'],
                                'Invalid Gender'
                            ),
                    })}
                    onSubmit={(updatedCustomer, { setSubmitting }) => {
                        setSubmitting(true);
                        updateCustomer(id, updatedCustomer)
                            .then(res => {
                                successNotification(
                                    "Customer Updated",
                                    `${updatedCustomer.name} was updated successfully`
                                );
                                fetchCustomers();
                                onClose();
                            }).catch(err => {
                                errorNotification(
                                    err.code,
                                    err.response.data.message
                                );
                            }).finally(() => {
                                setSubmitting(false);
                            });
                    }}
                >
                    {({isValid, isSubmitting, dirty}) => {
                        return (
                            <Form>
                                <Stack spacing={"24px"}>
                                    <MyBoth/>
                                    <Button
                                        isDisabled={!(isValid && dirty) || isSubmitting}
                                        type="submit"
                                    >
                                        Update
                                    </Button>
                                </Stack>
                            </Form>
                        );
                    }}
                </Formik>
            </>
        )
    );
};

export default CustomerForm;
