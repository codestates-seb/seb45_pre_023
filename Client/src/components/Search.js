import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faMagnifyingGlass } from '@fortawesome/free-solid-svg-icons';

export default function Search() {
  return (
    <div className='border-2 border-solid border-gray'>
      <FontAwesomeIcon icon={faMagnifyingGlass} style={{color: "#c7c7c7"}} />
      <input placeholder="Search..."></input>
    </div>
  );
}
