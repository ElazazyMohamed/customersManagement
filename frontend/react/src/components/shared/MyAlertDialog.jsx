import {
    AlertDialog,
    AlertDialogBody,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogContent,
    AlertDialogOverlay,
    Button,
    useDisclosure,
} from '@chakra-ui/react';
import {
    errorNotification,
    successNotification,
} from "../../services/notification.js";
import {
    useRef,
} from "react";
import {
    deleteCustomer,
} from "../../services/client.js";

const MyAlertDialog = ({
    id,
    name,
    fetchCustomers,
}) => {

    const { isOpen, onOpen, onClose } = useDisclosure();
    const btnRef = useRef();
    const cancelRef = useRef();

    const handleDelete = () => {
        deleteCustomer(id)
        .then(res => {
            successNotification(
                "Deleting Customer",
                `${name} was deleted successfully`
            );
            fetchCustomers();
        }).catch(err => {
            errorNotification(
                err.code,
                err.response.data.message
            );
        }).finally(() => {
            onClose();
        });
    }

    return (
        <>
            <Button
                colorScheme="red"
                flex={1}
                fontSize={'sm'}
                rounded={'full'}
                ref={btnRef}
                onClick={onOpen}
            >
                Delete
            </Button>
            <AlertDialog
                isOpen={isOpen}
                leastDestructiveRef={cancelRef}
                onClose={onClose}
            >
                <AlertDialogOverlay />
                <AlertDialogContent>
                    <AlertDialogHeader fontSize="lg" fontWeight="bold">
                        Delete Customer
                    </AlertDialogHeader>

                    <AlertDialogBody>
                        Are you sure you want to deleter {name}? You can't undo this action.
                    </AlertDialogBody>

                    <AlertDialogFooter>
                        <Button ref={cancelRef} onClick={onClose}>
                            Cancel
                        </Button>
                        <Button
                            variantColor="red"
                            colorScheme="red"
                            fontSize={'sm'}
                            ml={3}
                            onClick={handleDelete}
                        >
                            Delete
                        </Button>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>
        </>
    );
}

export default MyAlertDialog;
