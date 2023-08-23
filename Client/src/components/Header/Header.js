import HeaderAfter from './HeaderAfter';
import HeaderBefore from './HeaderBefore';
import { useSelector } from 'react-redux';

export default function Header() {
  const Logintoken = useSelector((state) => state.logininfo.token);
  console.log(Logintoken)

  return <>{Logintoken ? <HeaderAfter /> : <HeaderBefore />}</>;
}
