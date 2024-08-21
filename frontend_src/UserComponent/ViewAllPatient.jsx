import { useState, useEffect } from "react";
import axios from "axios";
import React from "react";
import { Button, Modal } from "react-bootstrap";

const ViewAllPatient = () => {
  const [allPatient, setAllPatient] = useState([]);

  useEffect(() => {
    const getAllPatient = async () => {
      const allPatient = await retrieveAllPatient();
      if (allPatient) {
        setAllPatient(allPatient);
      }
    };

    getAllPatient();
  }, []);

  const retrieveAllPatient = async () => {
    const response = await axios.get("http://localhost:8080/api/patient/all");
    console.log(response.data);
    return response.data;
  };

  const deletePatient = (patientId) => {
    fetch("http://localhost:8080/api/user/delete/id?userId=" + patientId, {
      method: "GET",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    }).then((result) => {
      result.json().then((res) => {
        alert(res.responseMessage);
      });
    });

    window.location.reload(true);
  };

  const [showModalViewResponse, setShowModalViewResponse] = useState(false);

  const handleCloseViewResponse = () => setShowModalViewResponse(false);
  const handleShowViewResponse = () => setShowModalViewResponse(true);
  const [pets, setPets] = useState([]);

  const viewResponse = (pets) => {
    setPets(pets);
    handleShowViewResponse();
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
          <h2>All Owners</h2>
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
                  <th scope="col">First Name</th>
                  <th scope="col">Last Name</th>
                  <th scope="col">Email Id</th>
                  <th scope="col">Age</th>
                  <th scope="col">Phone No</th>
                  <th scope="col">Address</th>
                  <th scope="col">Pets</th>
                  <th scope="col">Action</th>
                </tr>
              </thead>
              <tbody>
                {allPatient.map((patient) => {
                  return (
                    <tr>
                      <td>
                        <b>{patient.firstName}</b>
                      </td>

                      <td>
                        <b>{patient.lastName}</b>
                      </td>
                      <td>
                        <b>{patient.emailId}</b>
                      </td>
                      <td>
                        <b>{patient.age}</b>
                      </td>

                      <td>
                        <b>{patient.contact}</b>
                      </td>

                      <td>
                        <b>
                          {patient.street +
                            " " +
                            patient.city +
                            " " +
                            patient.pincode}
                        </b>
                      </td>
                      <td>
                        <button
                          onClick={() => viewResponse(patient.pets)}
                          className="btn btn-sm bg-color custom-bg-text ms-2 mt-2"
                        >
                          View Pets
                        </button>
                      </td>
                      <td>
                        <button
                          className="btn bg-color custom-bg-text btn-sm"
                          onClick={() => deletePatient(patient.id)}
                        >
                          Delete
                        </button>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <Modal
        show={showModalViewResponse}
        onHide={handleCloseViewResponse}
        size="xl"
      >
        <Modal.Header closeButton className="bg-color custom-bg-text">
          <Modal.Title
            style={{
              borderRadius: "1em",
            }}
          >
            Pets
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="ms-3 mt-3 mb-3 me-3">
            <div className="table-responsive">
              <table className="table table-hover text-color text-center">
                <thead className="table-bordered border-color bg-color custom-bg-text">
                  <tr>
                    <th scope="col">Pet Name</th>
                    <th scope="col">Description</th>
                    <th scope="col">Species</th>
                    <th scope="col">Breed</th>
                    <th scope="col">Age</th>
                    <th scope="col">Status</th>
                  </tr>
                </thead>
                <tbody>
                  {pets.map((pet) => {
                    return (
                      <tr>
                        <td>
                          <b>{pet.name}</b>
                        </td>

                        <td>
                          <b>{pet.description}</b>
                        </td>
                        <td>
                          <b>{pet.species}</b>
                        </td>
                        <td>
                          <b>{pet.breed}</b>
                        </td>
                        <td>
                          <b>{pet.age}</b>
                        </td>
                        <td>
                          <b>{pet.status}</b>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseViewResponse}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default ViewAllPatient;
