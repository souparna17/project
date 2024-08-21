import Carousel from "./Carousel";
import axios from "axios";
import { useState, useEffect } from "react";
import Footer from "./Footer";
import DoctorCard from "../UserComponent/DoctorCard";

const HomePage = () => {
  const [allDoctor, setAllDoctor] = useState([]);

  const retrieveAllDoctor = async () => {
    const response = await axios.get("http://localhost:8080/api/doctor/all");
    console.log(response.data);
    return response.data;
  };

  const [locations, setLocations] = useState([]);

  const [locationId, setLocationId] = useState("");
  const [tempLocationId, setTempLocationId] = useState("");

  const retrieveAllLocations = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/location/fetch/all"
    );
    return response.data;
  };

  useEffect(() => {
    const getAllDoctor = async () => {
      const allDoctor = await retrieveAllDoctor();
      if (allDoctor) {
        setAllDoctor(allDoctor);
      }
    };

    const getSearchedDoctors = async () => {
      const allDoctor = await searchDoctors();
      if (allDoctor) {
        setAllDoctor(allDoctor);
      }
    };

    const getAllLocations = async () => {
      const resCategory = await retrieveAllLocations();
      if (resCategory) {
        setLocations(resCategory.locations);
      }
    };

    if (locationId !== "") {
      getSearchedDoctors();
    } else {
      getAllDoctor();
    }

    getAllLocations();
  }, [locationId]);

  const searchDoctors = async () => {
    const response = await axios.get(
      "http://localhost:8080/api/doctor/location-wise?locationId=" + locationId
    );
    return response.data;
  };

  const searchDoctorByLocation = (e) => {
    e.preventDefault();
    setLocationId(tempLocationId);

    setTempLocationId("");
  };

  return (
    <>
    <Carousel/>
    <div className="container-fluid mb-2">
      

      <div className="mt-2 mb-5">
        <h5 className="text-color-second text-center mt-3 ">
          Search Doctors in your city!!
        </h5>

        <div className="d-flex aligns-items-center justify-content-center">
          <div className="row">
            <div className="col">
              <div className="mt-3">
                <form class="row g-3">
                  <div class="col-auto">
                    <select
                      name="tempLocationId"
                      onChange={(e) => setTempLocationId(e.target.value)}
                      className="form-control"
                      required
                    >
                      <option value="">Select Location</option>

                      {locations.map((location) => {
                        return (
                          <option value={location.id}> {location.name} </option>
                        );
                      })}
                    </select>
                  </div>

                  <div class="col-auto">
                    <button
                      type="submit"
                      class="btn bg-color text-white mb-3"
                      onClick={searchDoctorByLocation}
                    >
                      Search
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>

        <div className="row ">
          <div className="col-md-12">
            <div className="row row-cols-1 row-cols-md-5 " >
              {allDoctor.map((doctor) => {
                return <DoctorCard item={doctor} />;
              })}
            </div>
          </div>
        </div>
      </div>
      <hr />
    </div>
    </>
  );
};

export default HomePage;
