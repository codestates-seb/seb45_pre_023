import { useState } from 'react';
import HeaderAfter from './HeaderAfter';
import HeaderBefore from './HeaderBefore';

export default function Header() {
  // eslint-disable-next-line no-unused-vars
  const [isLogin, setLogin] = useState(false);

  return (
    <>
      {isLogin ? <HeaderAfter /> : <HeaderBefore /> }
    </>
  );
}
