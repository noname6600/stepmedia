import React, { useEffect, useMemo, useRef, useState } from 'react';
import Transition from '@components/common/Transition';
import { AvatarEmpty } from '@images/index';
import i18next from 'i18next';

export default function UserMenu() {
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const [toggleEnable, setToggleEnable] = useState<boolean>(i18next.language === 'vi');

  const language = useMemo(() => {
    if (toggleEnable === undefined) {
      return i18next.language;
    }
    return toggleEnable ? 'vi' : 'en';
  }, [toggleEnable]);

  const trigger = useRef<HTMLButtonElement>(null);
  const dropdown = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const clickHandler = ({ target }: MouseEvent) => {
      if (!dropdownOpen || dropdown.current?.contains(target as Node) || trigger.current?.contains(target as Node))
        return;
      setDropdownOpen(false);
    };
    document.addEventListener('click', clickHandler);
    return () => document.removeEventListener('click', clickHandler);
  });

  useEffect(() => {
    const keyHandler = ({ keyCode }: KeyboardEvent) => {
      if (!dropdownOpen || keyCode !== 27) return;
      setDropdownOpen(false);
    };
    document.addEventListener('keydown', keyHandler);
    return () => document.removeEventListener('keydown', keyHandler);
  });

  useEffect(() => {
    i18next.changeLanguage(language);
  }, [language]);

  return (
    <div className="relative inline-flex">
      <button
        ref={trigger}
        className="inline-flex justify-center items-center group"
        aria-haspopup="true"
        onClick={() => setDropdownOpen(!dropdownOpen)}
        aria-expanded={dropdownOpen}
      >
        <img
          className="w-10 h-10 rounded-full border-blue-400 border-2"
          src={AvatarEmpty}
          alt="User"
          width="32"
          height="32"
        />
        <div className="flex items-center truncate">
          <span className="truncate ml-2 text-sm font-medium group-hover:text-slate-800">Guest</span>
          <svg className="w-3 h-3 shrink-0 ml-1 fill-current text-slate-400" viewBox="0 0 12 12">
            <path d="M5.9 11.4L.5 6l1.4-1.4 4 4 4-4L11.3 6z" />
          </svg>
        </div>
      </button>

      <Transition
        className="origin-top-right z-10 absolute top-full right-0 min-w-44 bg-white border border-slate-200 py-1.5 rounded shadow-lg overflow-hidden mt-1"
        show={dropdownOpen}
        enter="transition ease-out duration-200 transform"
        enterStart="opacity-0 -translate-y-2"
        enterEnd="opacity-100 translate-y-0"
        leave="transition ease-out duration-200"
        leaveStart="opacity-100"
        leaveEnd="opacity-0"
        appear={dropdownOpen}
      >
        <div ref={dropdown} onFocus={() => setDropdownOpen(true)} onBlur={() => setDropdownOpen(false)}>
          <div className="pt-0.5 pb-2 px-3 mb-1 border-b border-slate-200">
            <div className="font-medium text-slate-800">Guest</div>
          </div>
          <ul>
            <li>
              <div className="py-1 px-3">
                <label className="inline-flex relative items-center mr-5 cursor-pointer">
                  <input type="checkbox" className="sr-only peer" checked={toggleEnable} readOnly />
                  <div
                    onClick={() => {
                      setToggleEnable(!toggleEnable);
                    }}
                    className="w-11 h-6 bg-gray-200 rounded-full peer  
                    peer-focus:ring-green-300 
                     peer-checked:after:translate-x-full 
                    peer-checked:after:border-white after:content-[''] 
                    after:absolute after:top-0.5 after:left-[2px] after:bg-white
                     after:border-gray-300 after:border after:rounded-full after:h-5 
                     after:w-5 after:transition-all peer-checked:bg-green-600"
                  ></div>
                  <span className="ml-2 text-sm font-medium text-gray-900">{toggleEnable ? 'VI' : 'EN'}</span>
                </label>
              </div>
            </li>
          </ul>
        </div>
      </Transition>
    </div>
  );
}
