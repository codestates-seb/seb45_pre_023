import HeaderAfter from './HeaderAfter';
import HeaderBefore from './HeaderBefore';

export default function Header({ isLogin }) {
  return <>{isLogin ? <HeaderAfter /> : <HeaderBefore />}</>;
}
