import {
    Spinner,
    Text,
    Box,
    Wrap,
    WrapItem,
    useDisclosure,
    Button,
} from '@chakra-ui/react';
import SidebarWithHeader from "./components/shared/SideBar.jsx";
import {
    useEffect,
    useState,
} from "react";
import {
    getCustomers,
} from "./services/client.js";
import CardWithImage from "./components/CardWithImage.jsx";
import DrawerForm from "./components/DrawerForm.jsx";
import {
    errorNotification,
} from "./services/notification.js";

const App = () => {
    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);
    const AddIcon = () => "+";

    // Create Customer Drawer
    const {isOpen, onOpen, onClose } = useDisclosure();

    const fetchCustomers = () => {
        setLoading(true);
        getCustomers().then(res => {
            setCustomers(res.data);
        }).catch(err => {
            errorNotification(
                err.code,
                err.response.data.message
            );
        }).finally(() => {
            setLoading(false);
        });
    }

    useEffect(() => {
        fetchCustomers();
    }, []);

    if (loading) {
        return (
            <SidebarWithHeader>
                <Box display="flex" justifyContent="center" alignItems="center" height="100vh">
                    <Spinner
                        thickness="4px"
                        speed="0.65s"
                        emptyColor="gray.200"
                        color="blue.500"
                        size="xl"
                    />
                </Box>
            </SidebarWithHeader>
        );
    }

    const isCustomerEmpty = customers.length === 0;

    return (
        <SidebarWithHeader>
            <Button
                leftIcon={<AddIcon />}
                colorScheme="blue"
                onClick={onOpen}
            >
                Create Customer
            </Button>
            <DrawerForm
                formType={"create"}
                isOpen={isOpen}
                onClose={onClose}
                fetchCustomers={fetchCustomers}
            />
            {isCustomerEmpty ? (
                <Box display="flex" justifyContent="center" alignItems="center" height="100vh">
                    <Text>No Customers Available</Text>
                </Box>
            ) : (
                <Wrap justify={"center"} spacing={"30px"}>
                    {customers.map((customer, index) => (
                        <WrapItem key={index}>
                            <CardWithImage
                                {...customer}
                                imageNumber={index}
                                fetchCustomers={fetchCustomers}
                            />
                        </WrapItem>
                    ))}
                </Wrap>
            )}
        </SidebarWithHeader>
    );
}

export default App;

