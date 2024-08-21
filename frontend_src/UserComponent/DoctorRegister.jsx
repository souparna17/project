import { useState, useEffect } from "react";
import "react-toastify/dist/ReactToastify.css";
import { ToastContainer, toast } from "react-toastify";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const DoctorRegister = () => {
  let navigate = useNavigate();

  const [user, setUser] = useState({
    firstName: "",
    lastName: "",
    emailId: "",
    password: "",
    contact: "",
    street: "",
    city: "",
    pincode: "",
    role: "doctor",
    age: "",
    sex: "",
    specialist: "",
    experience: "",
    locationId: "",
  });

  const [errors, setErrors] = useState({});
  const [locations, setLocations] = useState([]);
  const [genders, setGenders] = useState([]);
  const [specialists, setSpecialists] = useState([]);
  const [selectedImage, setSelectedImage] = useState(null);

  useEffect(() => {
    const getAllLocations = async () => {
      const resCategory = await retrieveAllLocations();
      if (resCategory) {
        setLocations(resCategory.locations);
      }
    };
    getAllLocations();
  }, []);

  const retrieveAllLocations = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/location/fetch/all"
    );
    return response.data;
  };

  const retrieveAllGenders = async () => {
    const response = await axios.get("http://localhost:8080/api/user/gender");
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

    const getAllSpecialist = async () => {
      const allSpecialist = await retrieveAllSpecialist();
      if (allSpecialist) {
        setSpecialists(allSpecialist);
      }
    };

    getAllGenders();
    getAllSpecialist();
  }, []);

  const handleUserInput = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };

  const validate = () => {
    const newErrors = {};

    if (!user.firstName.trim()) newErrors.firstName = "First Name is required";
    if (!user.lastName.trim()) newErrors.lastName = "Last Name is required";
    if (!user.emailId.trim()) newErrors.emailId = "Email ID is required";
    if (!user.password.trim()) newErrors.password = "Password is required";
    if (!user.contact.trim()) newErrors.contact = "Contact number is required";
    if (!user.street.trim()) newErrors.street = "Street is required";
    if (!user.city.trim()) newErrors.city = "City is required";
    if (!user.pincode.trim()) newErrors.pincode = "Pincode is required";
    if (!user.age) newErrors.age = "Age is required";
    if (!user.sex) newErrors.sex = "Gender is required";
    if (!user.specialist) newErrors.specialist = "Specialist is required";
    if (!user.experience) newErrors.experience = "Experience is required";
    if (!user.locationId || user.locationId === "0") newErrors.locationId = "Location is required";
    if (!selectedImage) newErrors.image = "Image is required";

    setErrors(newErrors);

    return Object.keys(newErrors).length === 0;
  };

  const saveUser = (event) => {
    event.preventDefault();

    if (!validate()) {
      toast.error("Please fix the form errors.");
      return;
    }

    const formData = new FormData();
    formData.append("image", selectedImage);
    formData.append("firstName", user.firstName);
    formData.append("lastName", user.lastName);
    formData.append("emailId", user.emailId);
    formData.append("password", user.password);
    formData.append("contact", user.contact);
    formData.append("street", user.street);
    formData.append("city", user.city);
    formData.append("pincode", user.pincode);
    formData.append("role", user.role);
    formData.append("age", user.age);
    formData.append("sex", user.sex);
    formData.append("specialist", user.specialist);
    formData.append("experience", user.experience);
    formData.append("locationId", user.locationId);

    axios
      .post("http://localhost:8080/api/doctor/register", formData)
      .then((result) => {
        result.json().then((res) => {
          toast.success(res.responseMessage);
          setTimeout(() => {
            navigate("/home");
          }, 2000); // Redirect after 2 seconds
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
                {errors.firstName && <small className="text-danger">{errors.firstName}</small>}
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
                {errors.lastName && <small className="text-danger">{errors.lastName}</small>}
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
                {errors.emailId && <small className="text-danger">{errors.emailId}</small>}
              </div>

              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="locationId" className="form-label">
                  <b>Location</b>
                </label>
                <select
                  onChange={handleUserInput}
                  className="form-control"
                  name="locationId"
                  value={user.locationId}
                  required
                >
                  <option value="0">Select Location</option>
                  {locations.map((location) => (
                    <option key={location.id} value={location.id}>
                      {location.name}
                    </option>
                  ))}
                </select>
                {errors.locationId && <small className="text-danger">{errors.locationId}</small>}
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
                {errors.password && <small className="text-danger">{errors.password}</small>}
              </div>

              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="sex" className="form-label">
                  <b>User Gender</b>
                </label>
                <select
                  onChange={handleUserInput}
                  className="form-control"
                  name="sex"
                  value={user.sex}
                  required
                >
                  <option value="">Select Gender</option>
                  {genders.map((gender) => (
                    <option key={gender} value={gender}>
                      {gender}
                    </option>
                  ))}
                </select>
                {errors.sex && <small className="text-danger">{errors.sex}</small>}
              </div>

              <div className="col-md-6 mb-3 text-color">
                <label htmlFor="specialist" className="form-label">
                  <b>Specialist</b>
                </label>
                <select
                  onChange={handleUserInput}
                  className="form-control"
                  name="specialist"
                  value={user.specialist}
                  required
                >
                  <option value="">Select Specialist</option>
                  {specialists.map((s) => (
                    <option key={s} value={s}>
                      {s}
                    </option>
                  ))}
                </select>
                {errors.specialist && <small className="text-danger">{errors.specialist}</small>}
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
                {errors.contact && <small className="text-danger">{errors.contact}</small>}
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
                <label htmlFor="experience" className="form-label">
                  <b>Experience</b>
                </label>
                <input
                  type="number"
                  className="form-control"
                  id="experience"
                  name="experience"
                  onChange={handleUserInput}
                  value={user.experience}
                  required
                />
                {errors.experience && <small className="text-danger">{errors.experience}</small>}
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
                {errors.street && <small className="text-danger">{errors.street}</small>}
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
                {errors.city && <small className="text-danger">{errors.city}</small>}
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
                {errors.pincode && <small className="text-danger">{errors.pincode}</small>}
              </div>

              <div className="col-md-6 mb-3">
                <label htmlFor="image" className="form-label">
                  <b>Select Doctor Image</b>
                </label>
                <input
                  className="form-control"
                  type="file"
                  id="image"
                  name="image"
                  onChange={(e) => setSelectedImage(e.target.files[0])}
                  required
                />
                {errors.image && <small className="text-danger">{errors.image}</small>}
              </div>

              <div className="d-flex aligns-items-center justify-content-center">
                <input
                  type="submit"
                  className="btn bg-color custom-bg-text"
                  value="Register Doctor"
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

export default DoctorRegister;
