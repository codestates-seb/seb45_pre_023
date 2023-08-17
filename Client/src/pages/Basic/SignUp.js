import SignUpInfo from '../../components/Join/SignUp/SignUpInfo';
import SignUpForm from '../../components/Join/SignUp/SignUpForm';

export default function SignUp() {
  return (
    <div className="flex felx-row justify-center items-start w-screen bg-gray-100">
      <SignUpInfo />
      <SignUpForm />
    </div>
  );
}
