import { useState, useEffect } from "react";
import axios from "axios";
import React from "react";
import { Link } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";

const ViewDoctorAppointment = () => {
  const [allAppointments, setAllAppointments] = useState([]);

  const doctor = JSON.parse(sessionStorage.getItem("active-doctor"));

  useEffect(() => {
    const getAllAppointments = async () => {
      const allAppointments = await retrieveAllAppointments();
      if (allAppointments) {
        setAllAppointments(allAppointments);
      }
    };

    getAllAppointments();
  }, []);

  const retrieveAllAppointments = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/appointment/doctor/id?doctorId=" + doctor.id
    );
    console.log(response.data);
    return response.data;
  };

  const cancelAppointment = (appointmentId, status) => {
    fetch("http://localhost:8080/api/appointment/patient/update", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        appointmentId: appointmentId,
        status: status,
      }),
    }).then((result) => {
      console.log(result);
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
            window.location.reload(true);
          }, 1000); // Redirect after 3 seconds
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
          }, 1000); // Redirect after 3 seconds
        }
      });
    });
  };

  return (
    <div className="mt-3">
      <div
        className="card form-card ms-2 me-2 mb-5 custom-bg border-color "
        style={{
          height: "45rem",
        }}
      >
        <div className="card-header custom-bg-text text-center bg-color">
          <h2>All Appointments</h2>
        </div>
        <div
          className="card-body"
          style={{
            overflowY: "auto",
          }}
        >
          <div className="table-responsive">
            <table className="table table-hover text-color text-center">
              <thead className="table-bordered border-color bg-color custom-bg-text">
                <tr>
                  <th scope="col">Owner Name</th>
                  <th scope="col">Owner Contact</th>
                  <th scope="col">Pet Name </th>
                  <th scope="col">Problem</th>
                  <th scope="col">Doctor Name</th>
                  <th scope="col">Precription</th>
                  <th scope="col">Appointment Take Date</th>
                  <th scope="col">Appointment Date</th>
                  <th scope="col">Appointment Status</th>
                  <th scope="col">Appointment Price</th>
                  <th scope="col">Action</th>
                </tr>
              </thead>
              <tbody>
                {allAppointments.map((a) => {
                  return (
                    <tr>
                      <td>
                        <b>{a.patientName}</b>
                      </td>

                      <td>
                        <b>{a.patientContact}</b>
                      </td>
                      <td>
                        <b>{a.pet.name}</b>
                      </td>
                      <td>
                        <b>{a.problem}</b>
                      </td>
                      <td>
                        <b>{a.doctorName}</b>
                      </td>
                      <td>
                        <b>{a.prescription}</b>
                      </td>
                      <td>
                        <b>{a.date}</b>
                      </td>
                      <td>
                        <b>{a.appointmentDate}</b>
                      </td>
                      <td>
                        <b>{a.status}</b>
                      </td>
                      <td>
                        <b>{a.price}</b>
                      </td>
                      <td>
                        {(() => {
                          if (a.status === "Booked") {
                            return (
                              <div>
                                <Link
                                  to={`/doctor/appointment/${a.id}/update`}
                                  className="nav-link active btn btn-sm"
                                  aria-current="page"
                                >
                                  <b className="text-color">
                                    Update Appointment
                                  </b>
                                </Link>

                                <button
                                  className="btn bg-color custom-bg-text btn-sm mt-2"
                                  onClick={() =>
                                    cancelAppointment(a.id, "Cancel")
                                  }
                                >
                                  Cancel
                                </button>
                              </div>
                            );
                          }
                        })()}
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ViewDoctorAppointment;
