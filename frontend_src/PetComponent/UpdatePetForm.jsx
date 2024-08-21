import { useState } from "react";
import { ToastContainer, toast } from "react-toastify";
import { useNavigate, useLocation } from "react-router-dom";

const UpdatePetForm = () => {
  const owner = JSON.parse(sessionStorage.getItem("active-patient"));
  const location = useLocation();
  const existingPet = location.state;
  const [name, setName] = useState(existingPet.name);
  const [description, setDescription] = useState(existingPet.description);
  const [species, setSpecies] = useState(existingPet.species);
  const [age, setAge] = useState(existingPet.age);
  const [breed, setBreed] = useState(existingPet.breed);
  const [id, setId] = useState(existingPet.id);

  let navigate = useNavigate();

  const savePet = (e) => {
    let data = { id, name, description, species, age, breed };

    fetch("http://localhost:8080/api/owner/pet/update", {
      method: "PUT",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        //     Authorization: "Bearer " + admin_jwtToken,
      },
      body: JSON.stringify(data),
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
              navigate("/owner/pet/all");
            }, 2000); // Redirect after 3 seconds
          } else if (res.responseCode === 1) {
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
    e.preventDefault();
  };

  return (
    <div>
      <div class="mt-2 d-flex aligns-items-center justify-content-center">
        <div class="form-card border-color" style={{ width: "25rem" }}>
          <div className="container-fluid">
            <div
              className="card-header bg-color custom-bg-text mt-2 d-flex justify-content-center align-items-center"
              style={{
                borderRadius: "1em",
                height: "38px",
              }}
            >
              <h5 class="card-title">Update Pet</h5>
            </div>
            <div class="card-body text-color mt-3">
              <form>
                <div class="mb-3">
                  <label for="title" class="form-label">
                    <b>Pet Name</b>
                  </label>
                  <input
                    type="text"
                    class="form-control"
                    id="title"
                    placeholder="enter title.."
                    onChange={(e) => {
                      setName(e.target.value);
                    }}
                    value={name}
                  />
                </div>
                <div class="mb-3">
                  <label for="description" class="form-label">
                    <b>Pet Description</b>
                  </label>
                  <textarea
                    class="form-control"
                    id="description"
                    rows="3"
                    placeholder="enter description.."
                    onChange={(e) => {
                      setDescription(e.target.value);
                    }}
                    value={description}
                  />
                </div>

                <div class="mb-3">
                  <label for="title" class="form-label">
                    <b>Pet Age</b>
                  </label>
                  <input
                    type="text"
                    class="form-control"
                    id="title"
                    onChange={(e) => {
                      setAge(e.target.value);
                    }}
                    value={age}
                  />
                </div>

                <div class="mb-3">
                  <label for="title" class="form-label">
                    <b>Pet Species</b>
                  </label>
                  <input
                    type="text"
                    class="form-control"
                    id="title"
                    onChange={(e) => {
                      setSpecies(e.target.value);
                    }}
                    value={species}
                  />
                </div>

                <div class="mb-3">
                  <label for="title" class="form-label">
                    <b>Pet Breed</b>
                  </label>
                  <input
                    type="text"
                    class="form-control"
                    id="title"
                    onChange={(e) => {
                      setBreed(e.target.value);
                    }}
                    value={breed}
                  />
                </div>

                <div className="d-flex aligns-items-center justify-content-center mb-2">
                  <button
                    type="submit"
                    onClick={savePet}
                    class="btn bg-color custom-bg-text"
                  >
                    Update Pet
                  </button>
                </div>

                <ToastContainer />
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UpdatePetForm;
