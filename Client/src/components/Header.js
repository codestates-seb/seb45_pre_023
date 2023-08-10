import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars } from '@fortawesome/free-solid-svg-icons';
import { faStackOverflow } from '@fortawesome/free-brands-svg-icons';
import { Link } from 'react-router-dom';
import { RouteConst } from '../Interface/RouteConst';
import Search from './Search';

export default function Header() {
  return (
    <div className="flex flex-row justify-center items-center h-12 border border-solid border-black">
      <button className="border border-solid border-black">
        <FontAwesomeIcon icon={faBars} />
      </button>
      <FontAwesomeIcon icon="fa-solid fa-bars" />
      <Link to={RouteConst.Login} className="text-lg border border-solid border-black">
        <FontAwesomeIcon
          icon={faStackOverflow}
          style={{ color: '#fb9637', fontSize: '2rem' }}
        />
        <span className="border border-solid border-black">stack overflow</span>
      </Link>
      <button className="border border-solid border-black">About</button>
      <button className="border border-solid border-black">Products</button>
      <button className="border border-solid border-black">For Teams</button>
      <Search />
      <button className="border border-solid border-black">Log in</button>
      <button className="border border-solid border-black">Sign up</button>
    </div>
  );
}
