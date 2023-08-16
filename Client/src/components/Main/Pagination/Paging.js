import Pagination from 'react-js-pagination';
import './Paging.css';
import axios from 'axios';
import { useState } from 'react';

const Paging = ({setIsData}) => {

  const [page, setPage] = useState(1);

  const getPage = (page) => {
    setPage(page);
    return axios
      .get(
        `http://ec2-43-201-249-199.ap-northeast-2.compute.amazonaws.com/questions?page=${page}`
      )
      .then((res) => {
        setIsData(res.data.data);
        console.log(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  };


  //   activePage: 현재 페이지
  //   itemsCountPerPage: 한 페이지에 보여줄 아이템 개수
  //   totalItemsCount: 전체 아이템 개수
  //   pageRangeDisplayed: 페이지 범위
  //   prevPageText: 이전 버튼 텍스트
  //   nextPageText: 다음 버튼 텍스트
  //   onChange: 페이지 클릭 시 실행되는 함수

  return (
    <div className="flex justify-center">
      <Pagination
        activePage={page}
        itemsCountPerPage={10}
        totalItemsCount={450}
        pageRangeDisplayed={5}
        prevPageText={'‹'}
        nextPageText={'›'}
        onChange={getPage}
      />
    </div>
  );
};

export default Paging;
