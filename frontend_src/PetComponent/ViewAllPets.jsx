import { useState, useEffect } from "react";
import axios from "axios";
import React from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

const ViewAllPets = () => {
  const [pets, setPets] = useState([]);
  const admin_jwtToken = sessionStorage.getItem("admin-jwtToken");
  const owner = JSON.parse(sessionStorage.getItem("active-patient"));

  let navigate = useNavigate();

  useEffect(() => {
    const getAllPet = async () => {
      const allPets = await retrieveAllPet();
      if (allPets) {
        setPets(allPets.pets);
      }
    };

    getAllPet();
  }, []);

  const retrieveAllPet = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/owner/pet/fetch/all?userId=" + owner.id
    );
    console.log(response.data);
    return response.data;
  };

  const deletePet = (petId, e) => {
    fetch(
      "http://localhost:8080/api/owner/pet/delete?petId=" + petId,
      {
        method: "DELETE",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
          //   Authorization: "Bearer " + admin_jwtToken,
        },
      }
    )
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

  const updatePet = (pet) => {
    navigate("/owner/pet/update", { state: pet });
  };

  return (
    <div className="mt-3">
      <div
        className="card form-card ms-2 me-2 mb-5 shadow-lg"
        style={{
          height: "45rem",
        }}
      >
        <div
          className="card-header custom-bg-text text-center bg-color"
          style={{
            borderRadius: "1em",
            height: "50px",
          }}
        >
          <h2>My Pets</h2>
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
                  <th scope="col">Pet Name</th>
                  <th scope="col">Pet Description</th>
                  <th scope="col">Pet Species</th>
                  <th scope="col">Pet Breed</th>
                  <th scope="col">Pet Age</th>
                  <th scope="col">Action</th>
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
                        <button
                          onClick={() => updatePet(pet)}
                          className="btn btn-sm bg-color custom-bg-text ms-2"
                        >
                          Update
                        </button>

                        <button
                          onClick={() => deletePet(pet.id)}
                          className="btn btn-sm bg-color custom-bg-text ms-2"
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
    </div>
  );
};

export default ViewAllPets;
