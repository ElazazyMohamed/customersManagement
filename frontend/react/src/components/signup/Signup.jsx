import {Flex, Heading, Image, Link, Stack, Text} from "@chakra-ui/react";
import CustomerForm from "../shared/CustomerForm.jsx";

const Signup = () => {
    return (
        <Stack minH={'100vh'} direction={{ base: 'column', md: 'row' }}>
            <Flex p={8} flex={1} alignItems={'center'} justifyContent={'center'}>
                <Stack spacing={4} w={'full'} maxW={'md'}>
                    <Image
                        src={"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRPGYanSyGr5wxiv0xUTkNXiq1hEXxomOsl1g&s"}
                        boxSize={"200px"}
                        alt={"Customer Management Logo"}
                    />
                    <Heading fontSize={'2xl'} mb={15}>Register for an account</Heading>
                    <CustomerForm
                        formType={"create"}
                    />
                    <Link color={"blue.500"} href={"/"}>
                        Already have an account? Login now.
                    </Link>
                </Stack>
            </Flex>
            <Flex
                flex={1}
                p={10}
                flexDirection={"column"}
                alignItems={"center"}
                justifyContent={"center"}
                bgGradient={{sm: "linear(to-r, blue.600, purple.600)"}}
            >
                <Text fontSize={"6xl"} color={"white"} fontWeight={"bold"} mb={5}>
                    <Link href={"https://amigoscode.com/courses"}>
                        Enroll Now
                    </Link>
                </Text>
                <Image
                    alt={'Login Image'}
                    objectFit={"cover"}
                    src={
                        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTl1CHzlS0qCcMUEvzn_Vdbkdj08dCCO1L5CA&s'
                    }
                />
            </Flex>
        </Stack>
    );
}

export default Signup;