import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars } from '@fortawesome/free-solid-svg-icons';
import { faStackOverflow } from '@fortawesome/free-brands-svg-icons';
import { Link } from 'react-router-dom';
import { RouteConst } from '../Interface/RouteConst';

export default function Header() {
  return (
    <div className="flex">
      <button>
        <FontAwesomeIcon icon={faBars} />
      </button>
      <FontAwesomeIcon icon="fa-solid fa-bars" />
      <Link to={RouteConst.Login} className="text-lg">
        <FontAwesomeIcon
          icon={faStackOverflow}
          style={{ color: '#fb9637', fontSize: '2rem' }}
        />
        stack overflow
      </Link>
      <ol className="flex space-x-5">
        <li>About</li>
        <li>Products</li>
        <li>ForTeams</li>
      </ol>
    </div>
  );
}
