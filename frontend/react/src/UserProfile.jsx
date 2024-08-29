const UserProfile = (props) => {

    const gender = props.gender === "male" ? "men" : "women";

    return(
        <div>
            <h1>{props.name}</h1>
            <p>{props.age}</p>
            <img
                src={`https://randomuser.me/api/portraits/${gender}/${props.imageNumber}.jpg`}
            />
            {props.children}
        </div>
    )
}

export default UserProfile;