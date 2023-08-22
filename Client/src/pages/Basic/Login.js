import { useSelector } from 'react-redux';
import LoginForm from '../../components/Join/Login/LoginForm';
import FindModal from '../../components/Join/Login/Modal.js/FindModal';

export default function Login() {
  const CanModal = useSelector(state => state.findinfo.mode.modal)
  return (
    <div className="flex felx-row justify-center items-center w-screen h-screen bg-gray-100">
      <LoginForm />
      {CanModal && <FindModal />}
    </div>
  );
}
