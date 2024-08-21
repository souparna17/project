import { useState, useEffect } from "react";
import "react-toastify/dist/ReactToastify.css";
import { ToastContainer, toast } from "react-toastify";
import axios from "axios";

const UserRegister = () => {
  const [user, setUser] = useState({
    firstName: "",
    lastName: "",
    emailId: "",
    password: "",
    contact: "",
    street: "",
    city: "",
    pincode: "",
    role: "",
    age: "",
    sex: "",
    bloodGroup: "",
    specialist: "",
  });

  const [errors, setErrors] = useState({});

  if (document.URL.indexOf("admin") !== -1) {
    user.role = "admin";
  } else if (document.URL.indexOf("owner") !== -1) {
    user.role = "owner";
  } else if (document.URL.indexOf("doctor") !== -1) {
    user.role = "doctor";
  }

  console.log("ROLE FETCHED : " + user.role);

  const handleUserInput = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };

  const [genders, setGenders] = useState([]);
  const [bloodGroup, setBloodGroup] = useState([]);
  const [specialists, setSpecialists] = useState([]);

  const retrieveAllGenders = async () => {
    const response = await axios.get("http://localhost:8080/api/user/gender");
    return response.data;
  };

  const retrieveAllBloodGroups = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/patient/bloodgroup/all"
    );
    return response.data;
  };

  const retrieveAllSpecialist = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/doctor/specialist/all"
    );
    return response.data;
  };

  useEffect(() => {
    const getAllGenders = async () => {
      const allGenders = await retrieveAllGenders();
      if (allGenders) {
        setGenders(allGenders.genders);
      }
    };

    const getAllBloodGroup = async () => {
      const allBloodGroups = await retrieveAllBloodGroups();
      if (allBloodGroups) {
        setBloodGroup(allBloodGroups);
      }
    };

    const getAllSpecialist = async () => {
      const allSpecialist = await retrieveAllSpecialist();
      if (allSpecialist) {
        setSpecialists(allSpecialist);
      }
    };

    getAllGenders();
    getAllBloodGroup();
    getAllSpecialist();
  }, []);

  const validate = () => {
    const validationErrors = {};
    if (!user.firstName) validationErrors.firstName = "First Name is required";
    if (!user.lastName) validationErrors.lastName = "Last Name is required";
    if (!user.emailId) {
      validationErrors.emailId = "Email is required";
    } else if (!/\S+@\S+\.\S+/.test(user.emailId)) {
      validationErrors.emailId = "Email format is invalid";
    }
    if (!user.password || user.password.length < 6) {
      validationErrors.password =
        "Password is required and should be at least 6 characters";
    }
    if (!user.contact || user.contact.length !== 10) {
      validationErrors.contact = "Contact number should be 10 digits";
    }
    if (!user.age || user.age < 18 || user.age > 100) {
      validationErrors.age = "Age should be between 18 and 100";
    }
    if (!user.street) validationErrors.street = "Street is required";
    if (!user.city) validationErrors.city = "City is required";
    if (!user.pincode || user.pincode.length !== 6) {
      validationErrors.pincode = "Pincode should be 6 digits";
    }
    if (!user.sex || user.sex === "0") validationErrors.sex = "Please select gender";

    setErrors(validationErrors);
    return Object.keys(validationErrors).length === 0;
  };

  const saveUser = (event) => {
    event.preventDefault();

    if (!validate()) {
      toast.error("Please fix the validation errors", {
        position: "top-center",
        autoClose: 1000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });
      return;
    }

    fetch("http://localhost:8080/api/user/register", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(user),
    }).then((result) => {
      toast.success("Registered Successfully!!!", {
        position: "top-center",
        autoClose: 1000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
      });

      result
        .json()
        .then((res) => {
          console.log("response", res);
        })
        .catch((error) => {
          console.log(error);
        });
    });
  };

  return (
    <div>
      <div className="mt-2 d-flex aligns-items-center justify-content-center ms-2 me-2 mb-2">
        <div
          className="card form-card border-color text-color custom-bg"
          style={{ width: "50rem" }}
        >
          <div className="card-header bg-color custom-bg-text text-center">
            <h5 className="card-title">Register {user.role}</h5>
          </div>
          <div className="card-body">
            <form className="row g-3" onSubmit={saveUser}>
              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="firstName" className="form-label">
                  <b>First Name</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="firstName"
                  name="firstName"
                  onChange={handleUserInput}
                  value={user.firstName}
                  required
                />
                {errors.firstName && (
                  <small className="text-danger">{errors.firstName}</small>
                )}
              </div>
              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="lastName" className="form-label">
                  <b>Last Name</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="lastName"
                  name="lastName"
                  onChange={handleUserInput}
                  value={user.lastName}
                  required
                />
                {errors.lastName && (
                  <small className="text-danger">{errors.lastName}</small>
                )}
              </div>
              <div className="col-md-6 mb-3 text-color">
                <b>
                  <label className="form-label">Email Id</label>
                </b>
                <input
                  type="email"
                  className="form-control"
                  id="emailId"
                  name="emailId"
                  onChange={handleUserInput}
                  value={user.emailId}
                  required
                />
                {errors.emailId && (
                  <small className="text-danger">{errors.emailId}</small>
                )}
              </div>
              <div className="col-md-6 mb-3">
                <label htmlFor="password" className="form-label">
                  <b>Password</b>
                </label>
                <input
                  type="password"
                  className="form-control"
                  id="password"
                  name="password"
                  onChange={handleUserInput}
                  value={user.password}
                  required
                />
                {errors.password && (
                  <small className="text-danger">{errors.password}</small>
                )}
              </div>
              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="sex" className="form-label">
                  <b>User Gender</b>
                </label>
                <select
                  onChange={handleUserInput}
                  className="form-control"
                  name="sex"
                  required
                >
                  <option value="0">Select Gender</option>
                  {genders.map((gender) => {
                    return <option key={gender} value={gender}>{gender}</option>;
                  })}
                </select>
                {errors.sex && (
                  <small className="text-danger">{errors.sex}</small>
                )}
              </div>

              <div className="col-md-6 mb-3">
                <label htmlFor="contact" className="form-label">
                  <b>Contact No</b>
                </label>
                <input
                  type="number"
                  className="form-control"
                  id="contact"
                  name="contact"
                  onChange={handleUserInput}
                  value={user.contact}
                  required
                />
                {errors.contact && (
                  <small className="text-danger">{errors.contact}</small>
                )}
              </div>
              <div className="col-md-6 mb-3">
                <label htmlFor="age" className="form-label">
                  <b>Age</b>
                </label>
                <input
                  type="number"
                  className="form-control"
                  id="age"
                  name="age"
                  onChange={handleUserInput}
                  value={user.age}
                  required
                />
                {errors.age && <small className="text-danger">{errors.age}</small>}
              </div>
              <div className="col-md-6 mb-3">
                <label htmlFor="street" className="form-label">
                  <b>Street</b>
                </label>
                <textarea
                  className="form-control"
                  id="street"
                  name="street"
                  rows="3"
                  onChange={handleUserInput}
                  value={user.street}
                  required
                />
                {errors.street && (
                  <small className="text-danger">{errors.street}</small>
                )}
              </div>
              <div className="col-md-6 mb-3">
                <label htmlFor="city" className="form-label">
                  <b>City</b>
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="city"
                  name="city"
                  onChange={handleUserInput}
                  value={user.city}
                  required
                />
                {errors.city && (
                  <small className="text-danger">{errors.city}</small>
                )}
              </div>
              <div className="col-md-6 mb-3">
                <label htmlFor="pincode" className="form-label">
                  <b>Pincode</b>
                </label>
                <input
                  type="number"
                  className="form-control"
                  id="pincode"
                  name="pincode"
                  onChange={handleUserInput}
                  value={user.pincode}
                  required
                />
                {errors.pincode && (
                  <small className="text-danger">{errors.pincode}</small>
                )}
              </div>
              <div className="d-flex aligns-items-center justify-content-center">
                <input
                  type="submit"
                  className="btn bg-color custom-bg-text"
                  value="Register User"
                  required
                />
              </div>
              <ToastContainer />
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserRegister;
