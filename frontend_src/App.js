import "./App.css";
import { Route, Routes } from "react-router-dom";
import AboutUs from "./page/AboutUs";
import ContactUs from "./page/ContactUs";
import Header from "./NavbarComponent/Header";
import HomePage from "./page/HomePage";
import UserRegister from "./UserComponent/UserRegister";
import UserLoginForm from "./UserComponent/UserLoginForm";
import AddAppointment from "./AppointmentComponent/AddAppointment";
import ViewMyAppointment from "./AppointmentComponent/ViewMyAppointment";
import ViewAllAppointment from "./AppointmentComponent/ViewAllAppointment";
import AssignAppointment from "./AppointmentComponent/AssignAppointment";
import ViewAllDoctor from "./UserComponent/ViewAllDoctor";
import ViewAllPatient from "./UserComponent/ViewAllPatient";
import ViewDoctorAppointment from "./AppointmentComponent/ViewDoctorAppointment";
import TreatAppointment from "./AppointmentComponent/TreatAppointment";
import DoctorRegister from "./UserComponent/DoctorRegister";
import AddLocationForm from "./LocationComponent/AddLocationForm";
import ViewAllLocations from "./LocationComponent/ViewAllLocations";
import UpdateLocationForm from "./LocationComponent/UpdateLocationForm";
import ViewAllPets from "./PetComponent/ViewAllPets";
import AddPetForm from "./PetComponent/AddPetForm";
import MyWallet from "./UserComponent/MyWallet";
import UserProfilePage from "./UserComponent/UserProfilePage";
import UpdatePetForm from "./PetComponent/UpdatePetForm";
import UpdateProfilePage from "./UserComponent/UpdateProfilePage";

function App() {
  return (
    <div>
      <Header />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/home" element={<HomePage />} />
        <Route path="/home/all/hotel/location" element={<HomePage />} />
        <Route path="contact" element={<ContactUs />} />
        <Route path="about" element={<AboutUs />} />
        <Route path="user/doctor/register" element={<DoctorRegister />} />
        <Route path="user/owner/register" element={<UserRegister />} />
        <Route path="user/admin/register" element={<UserRegister />} />
        <Route path="/user/login" element={<UserLoginForm />} />
        <Route path="/patient/appointment/take" element={<AddAppointment />} />
        <Route path="/patient/appointments/" element={<ViewMyAppointment />} />
        <Route path="/user/doctor/all" element={<ViewAllDoctor />} />
        <Route path="/user/patient/all" element={<ViewAllPatient />} />
        <Route path="/patient/appointments/" element={<ViewMyAppointment />} />
        <Route
          path="/doctor/appointment/all"
          element={<ViewDoctorAppointment />}
        />
        <Route
          path="/admin/appointments/all"
          element={<ViewAllAppointment />}
        />
        <Route
          path="/admin/appointment/:appointmentId/assign"
          element={<AssignAppointment />}
        />
        <Route
          path="/doctor/appointment/:appointmentId/update"
          element={<TreatAppointment />}
        />
        <Route path="/admin/location/add" element={<AddLocationForm />} />
        <Route path="/admin/location/all" element={<ViewAllLocations />} />
        <Route path="/admin/location/update" element={<UpdateLocationForm />} />
        <Route path="/owner/pet/all" element={<ViewAllPets />} />
        <Route path="/owner/pet/add" element={<AddPetForm />} />
        <Route path="/customer/wallet" element={<MyWallet />} />
        <Route
          path="/user/:userId/profile/detail"
          element={<UserProfilePage />}
        />
        <Route path="/owner/pet/update" element={<UpdatePetForm />} />
        <Route path="/user/profile/update" element={<UpdateProfilePage />} />
      </Routes>
    </div>
  );
}

export default App;
