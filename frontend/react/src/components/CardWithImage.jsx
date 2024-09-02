'use client'
import {
    Heading,
    Avatar,
    Box,
    Center,
    Text,
    Stack,
    Button,
    Badge,
    Tag,
    useColorModeValue,
    useDisclosure,
} from '@chakra-ui/react'
import MyAlertDialog from "./shared/MyAlertDialog.jsx";
import DrawerForm from "./DrawerForm.jsx";

const CardWithImage = ({
    id,
    name,
    email,
    age,
    gender,
    imageNumber,
    fetchCustomers,
}) => {
    const randomUserGender = gender === 'Male' ? 'men' : 'women';

    // Update Customer Drawer
    const { isOpen, onOpen, onClose } = useDisclosure();

    return (
        <Center py={6}>
            <Box
                maxW={'300px'}
                w={'full'}
                bg={useColorModeValue('white', 'gray.900')}
                boxShadow={'2xl'}
                rounded={'lg'}
                p={6}
                textAlign={'center'}
            >
                <Avatar
                    size={'xl'}
                    src={`https://randomuser.me/api/portraits/${randomUserGender}/${imageNumber}.jpg`}
                    mb={4}
                    pos={'relative'}
                    _after={{
                        content: '""',
                        w: 4,
                        h: 4,
                        bg: 'green.300',
                        border: '2px solid white',
                        rounded: 'full',
                        pos: 'absolute',
                        bottom: 0,
                        right: 3,
                    }}
                />
                <Heading fontSize={'2xl'} fontFamily={'body'}>
                    <Tag borderRadius={"full"}>{id}</Tag>
                    {name}
                </Heading>
                <Text fontWeight={600} color={'gray.500'} mb={4}>
                    {email}
                </Text>
                <Text
                    textAlign={'center'}
                    color={useColorModeValue('gray.700', 'gray.400')}
                    px={3}
                >
                    My name is {name}. You can reach me on:
                    <Text color={'blue.400'}>{email}</Text>
                </Text>
                <Badge
                    px={2}
                    py={1}
                    bg={useColorModeValue('gray.50', 'gray.800')}
                    fontWeight={'400'}
                >
                    Age: {age} | {gender}
                </Badge>
                <Stack mt={8} direction={'row'} spacing={4}>
                    <MyAlertDialog
                        id={id}
                        name={name}
                        fetchCustomers={fetchCustomers}
                    />
                    <Button
                        colorScheme="blue"
                        flex={1}
                        fontSize={'sm'}
                        rounded={'full'}
                        onClick={onOpen}
                    >
                        Update
                    </Button>
                    <DrawerForm
                        formType={"update"}
                        isOpen={isOpen}
                        onClose={onClose}
                        id={id}
                        initialValues={{ name, email, age, gender }}
                        fetchCustomers={fetchCustomers}
                    />
                </Stack>
            </Box>
        </Center>
    );
}

export default CardWithImage;
