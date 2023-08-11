import { useState } from 'react';
import HeaderAfter from './HeaderAfter';
import HeaderBefore from './HeaderBefore';

export default function Header() {
  const [isLogin, setLogin] = useState(false);

  return (
    <>
      {isLogin ? <HeaderAfter /> : <HeaderBefore /> }
    </>
  );
}
