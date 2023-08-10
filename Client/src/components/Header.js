import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars } from '@fortawesome/free-solid-svg-icons';
import { faStackOverflow } from '@fortawesome/free-brands-svg-icons';
import { Link } from 'react-router-dom';
import { RouteConst } from '../Interface/RouteConst';
import Search from './Search';

import { useState } from 'react';
import HeaderAfter from './HeaderAfter';
import HeaderBefore from './HeaderBefore';

export default function Header() {
  const [isLogin, setLogin] = useState(true);

  return (
    <>
      {isLogin ? <HeaderAfter /> : <HeaderBefore /> }
    </>
  );
}
