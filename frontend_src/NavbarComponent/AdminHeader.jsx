import { Link, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const AdminHeader = () => {
  let navigate = useNavigate();

  const user = JSON.parse(sessionStorage.getItem("active-admin"));
  console.log(user);

  const adminLogout = () => {
    toast.success("logged out!!!", {
      position: "top-center",
      autoClose: 1000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
    });
    sessionStorage.removeItem("active-admin");
    
    navigate("/home");
    window.location.reload(true);
  };



  return (
    <ul class="navbar-nav ms-auto mb-2 mb-lg-0 me-5">
      <li class="nav-item dropdown">

        <a
          class="nav-link dropdown-toggle text-color"
          href="#"
          id="navbarDropdown"
          role="button"
          data-bs-toggle="dropdown"
          aria-expanded="false"
        >
          <b> Location</b>
        </a>


        <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
          <li>
            <Link
              to="/admin/location/add"
              class="nav-link active"
              aria-current="page"
            >
              <b className="text-color"> Add Location</b>
            </Link>
          </li>
          <li>
            <Link
              to="/admin/location/all"
              class="nav-link active"
              aria-current="page"
            >
              <b className="text-color">View Locations</b>
            </Link>
          </li>
        </ul>
      </li>

      <li class="nav-item dropdown">

        <a
          class="nav-link dropdown-toggle text-color"
          href="#"
          id="navbarDropdown"
          role="button"
          data-bs-toggle="dropdown"
          aria-expanded="false"
        >
          <b> Doctors</b>
        </a>


        <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
          <li>
            <Link
              to="/user/doctor/register"
              class="nav-link active"
              aria-current="page"
            >
              <b className="text-color"> Register Doctors</b>
            </Link>
          </li>
          <li>
            <Link
              to="user/doctor/all"
              class="nav-link active"
              aria-current="page"
            >
              <b className="text-color">View Doctors</b>
            </Link>
          </li>
        </ul>
      </li>


      <li className="nav-item">
        <Link
          to="/user/patient/all"
          className="nav-link active"
          aria-current="page"
        >
          <b className="text-color">Patients</b>
        </Link>
      </li>


      <li className="nav-item">
        <Link
          to="admin/appointments/all"
          className="nav-link active"
          aria-current="page"
        >
          <b className="text-color">Appointments</b>
        </Link>
      </li>

      <button class="nav-item btn bg-color text-white mb-1">
        <Link
          class="nav-link active"
          aria-current="page"
          onClick={adminLogout}
        >
          <b className="text-color">Logout</b>
        </Link>
        <ToastContainer />
      </button>
    </ul>
  );
};

export default AdminHeader;
