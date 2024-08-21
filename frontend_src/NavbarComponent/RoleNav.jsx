import AdminHeader from "./AdminHeader";
import NormalHeader from "./NormalHeader";
import DoctorHeader from "./DoctorHeader";
import OwnerHeader from "./OwnerHeader";

const RoleNav = () => {
  const doctor = JSON.parse(sessionStorage.getItem("active-doctor"));
  const admin = JSON.parse(sessionStorage.getItem("active-admin"));
   const patient = JSON.parse(sessionStorage.getItem("active-patient"));


  if (patient != null) {
    return <OwnerHeader />;
  } else if (admin != null) {
    return <AdminHeader />;
  } else if (doctor != null) {
    return <DoctorHeader />;
  }
   else {
    return <NormalHeader />;
  }
};

export default RoleNav;
