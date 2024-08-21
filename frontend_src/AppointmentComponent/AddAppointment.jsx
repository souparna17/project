import { useState, useEffect } from "react";
import "react-toastify/dist/ReactToastify.css";
import { ToastContainer, toast } from "react-toastify";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const AddAppointment = () => {
  const patient = JSON.parse(sessionStorage.getItem("active-patient"));

  let navigate = useNavigate();

  const [allDoctor, setAllDoctor] = useState([]);
  const [pets, setPets] = useState([]);

  const retrieveAllDoctor = async () => {
    const response = await axios.get("http://localhost:8080/api/doctor/all");
    console.log(response.data);
    return response.data;
  };

  useEffect(() => {
    const getAllDoctor = async () => {
      const allDoctor = await retrieveAllDoctor();
      if (allDoctor) {
        setAllDoctor(allDoctor);
      }
    };

    const getAllPet = async () => {
      const allPets = await retrieveAllPet();
      if (allPets) {
        setPets(allPets.pets);
      }
    };

    getAllPet();

    getAllDoctor();
  }, []);

  const retrieveAllPet = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/owner/pet/fetch/all?userId=" + patient.id
    );
    console.log(response.data);
    return response.data;
  };

  const [appointment, setAppointment] = useState({
    patientId: "",
    problem: "",
    doctorId: "",
    petId: "",
    appointmentDate: "",
    appointmentTime: "",
  });

  appointment.patientId = patient.id;

  const handleUserInput = (e) => {
    setAppointment({ ...appointment, [e.target.name]: e.target.value });
  };

  const saveAppointment = (event) => {
    event.preventDefault();
    fetch("http://localhost:8080/api/appointment/patient/add", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(appointment),
    })
      .then((result) => {
        result.json().then((res) => {
          if (res.responseCode === 0) {
            toast.success(res.responseMessage, {
              position: "top-center",
              autoClose: 1000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            });

            setTimeout(() => {
              navigate("/home");
            }, 2000); // Redirect after 3 seconds
          } else if (res.responseCode === 1) {
            toast.error(res.responseMessage, {
              position: "top-center",
              autoClose: 1000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            });
            setTimeout(() => {
              window.location.reload(true);
            }, 2000); // Redirect after 3 seconds
          } else {
            toast.error("It Seems Server is down!!!", {
              position: "top-center",
              autoClose: 1000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
            });
            setTimeout(() => {
              window.location.reload(true);
            }, 2000); // Redirect after 3 seconds
          }
        });
      })
      .catch((error) => {
        console.error(error);
        toast.error("It seems server is down", {
          position: "top-center",
          autoClose: 1000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
        setTimeout(() => {
          window.location.reload(true);
        }, 1000); // Redirect after 3 seconds
      });
  };

  return (
    <div>
      <div className="mt-2 d-flex aligns-items-center justify-content-center ms-2 me-2 mb-2">
        <div
          className="card form-card border-color text-color custom-bg"
          style={{ width: "25rem" }}
        >
          <div className="card-header bg-color custom-bg-text text-center">
            <h5 className="card-title">Take Appointment</h5>
          </div>
          <div className="card-body">
            <form onSubmit={saveAppointment}>
              <div className=" mb-3 text-color">
                <label htmlFor="sex" className="form-label">
                  <b>Pet</b>
                </label>
                <select
                  onChange={handleUserInput}
                  className="form-control"
                  name="petId"
                >
                  <option value="0">Select Pet</option>

                  {pets.map((pet) => {
                    return <option value={pet.id}> {pet.name} </option>;
                  })}
                </select>
              </div>

              <div className="mb-3 text-color">
                <label htmlFor="sex" className="form-label">
                  <b>Doctor</b>
                </label>
                <select
                  onChange={handleUserInput}
                  className="form-control"
                  name="doctorId"
                >
                  <option value="0">Select Doctor</option>

                  {allDoctor.map((doctor) => {
                    return (
                      <option value={doctor.id}>
                        {" "}
                        {doctor.firstName +
                          " " +
                          doctor.lastName +
                          " [" +
                          doctor.specialist +
                          "]"}{" "}
                      </option>
                    );
                  })}
                </select>
              </div>

              <div className="mb-3 text-color">
                <label htmlFor="title" className="form-label">
                  <b>Problem</b>
                </label>
                <textarea
                  type="text"
                  className="form-control"
                  id="problem"
                  name="problem"
                  onChange={handleUserInput}
                  value={appointment.problem}
                  placeholder="mention your problems here..."
                />
              </div>
              <div className="mb-3 text-color">
                <label htmlFor="description" className="form-label">
                  <b>Appointment Date</b>
                </label>
                <input
                  type="date"
                  className="form-control"
                  id="appointmentDate"
                  name="appointmentDate"
                  onChange={handleUserInput}
                  value={appointment.appointmentDate}
                />
              </div>
              <div className="mb-3 text-color">
                <label htmlFor="description" className="form-label">
                  <b>Appointment Time</b>
                </label>
                <select
                  name="appointmentTime"
                  onChange={handleUserInput}
                  className="form-control"
                >
                  <option value="">Select Appointment Time</option>
                  <option value="09:00 - 10:00 am">09:00 - 10:00 am</option>
                  <option value="10:00 - 11:00 am">10:00 - 11:00 am</option>
                  <option value="11:00 - 12:00 am">11:00 - 12:00 am</option>
                  <option value="12:00 - 01:00 pm">12:00 - 01:00 pm</option>
                  <option value="01:00 - 02:00 pm">01:00 - 02:00 pm</option>
                  <option value="02:00 - 03:00 pm">02:00 - 03:00 pm</option>
                  <option value="03:00 - 04:00 pm">03:00 - 04:00 pm</option>
                  <option value="04:00 - 05:00 pm">04:00 - 05:00 pm</option>
                  <option value="05:00 - 06:00 pm">05:00 - 06:00 pm</option>
                  <option value="06:00 - 07:00 pm">06:00 - 07:00 pm</option>
                  <option value="07:00 - 08:00 pm">07:00 - 08:00 pm</option>
                  <option value="08:00 - 09:00 pm">08:00 - 09:00 pm</option>
                  <option value="09:00 - 10:00 pm">09:00 - 10:00 pm</option>
                </select>
              </div>

              <input
                type="submit"
                className="btn bg-color custom-bg-text"
                value="Take Appointment"
              />

              <ToastContainer />
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AddAppointment;
