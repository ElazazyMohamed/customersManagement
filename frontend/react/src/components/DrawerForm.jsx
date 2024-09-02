import {
    Button,
    Drawer,
    DrawerBody,
    DrawerFooter,
    DrawerHeader,
    DrawerOverlay,
    DrawerContent,
    DrawerCloseButton,
} from '@chakra-ui/react'
import CustomerForm from "./CustomerForm.jsx";

const CloseIcon = () => "x";

const DrawerForm = ({
    fetchCustomers,
    isOpen,
    onClose,
    formType,
    id,
    initialValues
}) => {

    return (
        <>
            <Drawer
                isOpen={isOpen}
                onClose={onClose}
                size={"xl"}
            >
                <DrawerOverlay />
                <DrawerContent>
                    <DrawerCloseButton />
                    {formType === "create" ? (
                        <>
                            <DrawerHeader>Create a new Customer</DrawerHeader>
                            <DrawerBody>
                                <CustomerForm
                                    fetchCustomers={fetchCustomers}
                                    formType={formType}
                                    onClose={onClose}
                                />
                            </DrawerBody>
                        </>
                    ) : (
                        <>
                            <DrawerHeader>Update an existing Customer</DrawerHeader>
                            <DrawerBody>
                                <CustomerForm
                                    fetchCustomers={fetchCustomers}
                                    formType={formType}
                                    onClose={onClose}
                                    id={id}
                                    initialValues={initialValues}
                                />
                            </DrawerBody>
                        </>
                    )}
                    <DrawerFooter>
                        <Button
                            leftIcon={<CloseIcon />}
                            colorScheme={"teal"}
                            onClick={onClose}
                        >
                            Close
                        </Button>
                    </DrawerFooter>
                </DrawerContent>
            </Drawer>
        </>
    );
}

export default DrawerForm;
