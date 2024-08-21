import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";

const UserProfilePage = () => {
  const { userId } = useParams();

  let navigate = useNavigate();

  const [locations, setLocations] = useState([]);
  const [specialists, setSpecialists] = useState([]);

  const retrieveAllSpecialist = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/doctor/specialist/all"
    );
    return response.data;
  };

  const [user, setUser] = useState({
    id: "",
    firstName: "",
    lastName: "",
    shopName: "",
    emailId: "",
    phoneNo: "",
    role: "",

    street: "",
    city: "",
    pincode: "",

    walletAmount: "",
    status: "",
  });

  useEffect(() => {
    const getUser = async () => {
      const userRes = await retrieveUser();
      if (userRes) {
        setUser(userRes.user);
      }
    };

    getUser();
  }, [userId]);

  const retrieveUser = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/user/id?userId=" + userId
    );
    return response.data;
  };

  const updateProfile = (user) => {
    navigate("/user/profile/update", { state: user });
  };

  return (
    <div className="container-fluid mb-2">
      <div className="container-fluid mb-2">
        <div className="d-flex align-items-center justify-content-center ms-5 mt-1 me-5 mb-3">
          <div
            className="h-100"
            style={{
              width: "900px",
            }}
          >
            <div className="card-body text-color">
              <div className="text-center header-logo-color">
                <h3 className="mt3">My Profile</h3>
              </div>

              <div className="row mt-4">
                <div className="col-md-4">
                  <p className="mb-2">
                    <b>First Name:</b> {user.firstName}
                  </p>
                </div>
                <div className="col-md-4">
                  <p className="mb-2">
                    <b>Last Name:</b> {user.lastName}
                  </p>
                </div>
                <div className="col-md-4">
                  <p className="mb-2">
                    <b>Email Id:</b> {user.emailId}
                  </p>
                </div>
              </div>
              <div className="row mt-4">
                <div className="col-md-4">
                  <p className="mb-2">
                    <b>Address:</b>{" "}
                    {user.street + ", " + user.city + ", " + user.pincode}
                  </p>
                </div>

                <div className="col-md-4">
                  <p className="mb-2">
                    <b>Wallet Amount:</b> &#8377; {user.walletAmount}
                  </p>
                </div>
              </div>

              <div className="mt-2 d-flex aligns-items-center justify-content-center">
                <button
                  className="btn bg-color custom-bg-text btn-sm"
                  onClick={() => updateProfile(user)}
                >
                  Update Profile
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserProfilePage;
