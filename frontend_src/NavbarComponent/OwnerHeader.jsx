import { Link, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const OwnerHeader = () => {
  let navigate = useNavigate();

  const user = JSON.parse(sessionStorage.getItem("active-patient"));

  const userLogout = () => {
    toast.success("logged out!!!", {
      position: "top-center",
      autoClose: 2000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
    });
    sessionStorage.removeItem("active-patient");

    navigate("/home");
    window.location.reload(true);
  };

  const viewProfile = (e) => {
    navigate(`/user/${user.id}/profile/detail`);
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
          <b> Pet</b>
        </a>
        <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
          <li>
            <Link
              to="/owner/pet/add"
              class="nav-link active"
              aria-current="page"
            >
              <b className="text-color"> Add Pet</b>
            </Link>
          </li>
          <li>
            <Link
              to="/owner/pet/all"
              class="nav-link active"
              aria-current="page"
            >
              <b className="text-color">View Pets</b>
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
          <b> Appoitments</b>
        </a>
        <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
          <li>
            <Link
              to="patient/appointment/take"
              class="nav-link active"
              aria-current="page"
            >
              <b className="text-color"> Take Appointment</b>
            </Link>
          </li>
          <li>
            <Link
              to="patient/appointments"
              class="nav-link active"
              aria-current="page"
            >
              <b className="text-color">View Appointments</b>
            </Link>
          </li>
        </ul>
      </li>


      <li class="nav-item">
        <div class="nav-link active" aria-current="page">
          <b className="text-color" onClick={viewProfile}>
            View Profile
          </b>
        </div>
      </li>

      <li>
        <Link to="/customer/wallet" class="nav-link active" aria-current="page">
          <b className="text-color"> Wallet Amount</b>
        </Link>
      </li>

      <li class="nav-item">
        <Link
          to=""
          class="nav-link active"
          aria-current="page"
          onClick={userLogout}
        >
          <b className="text-color">Logout</b>
        </Link>
        <ToastContainer />
      </li>
    </ul>
  );
};

export default OwnerHeader;
