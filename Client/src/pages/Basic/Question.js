import LeftSidebar from "../../components/SideBar/LeftSidebar"
import QuestionDetail from "../../components/Question/QuestionDetail"
import RightSidebar from "../../components/SideBar/RightSidebar"
import Answer from "../../components/Question/Answer/Answer"


export default function Question() {
    return (
        <>
        <div className="flex">
        <LeftSidebar />
        <div>
            <QuestionDetail />
        </div>
        <RightSidebar />
        <Answer />
      </div>
        </>
    )
}