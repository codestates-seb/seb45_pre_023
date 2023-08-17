import HeaderAfter from './HeaderAfter';
import HeaderBefore from './HeaderBefore';
import { useSelector } from 'react-redux';

export default function Header() {
  const OAuthtoken = useSelector((state) => state.oauth.token);
  const Logintoken = useSelector((state) => state.logininfo.token);

  return <>{OAuthtoken || Logintoken ? <HeaderAfter /> : <HeaderBefore />}</>;
}
