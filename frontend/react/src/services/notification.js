import {
    createStandaloneToast
} from '@chakra-ui/toast'

const { toast } = createStandaloneToast();

const notification = ({ title, description, status }) => {
    toast({
        title,
        description,
        status,
        isClosable: true,
        duration: 5000
    })
}

export const successNotification = (title, description) => {
    notification({
        title,
        description,
        status: "success"
    })
}

export const errorNotification = (title, description) => {
    notification({
        title,
        description,
        status: "error"
    })
}
