import React, { forwardRef, ReactNode, useEffect, useImperativeHandle, useRef, useState } from 'react';

import Transition from './Transition';

export interface ModalsController {
  open: (modal: Modal) => void;
  close: (key: string) => void;
}

export interface Modal {
  key: string;
  content: ReactNode | JSX.Element;
}

const ModalView = forwardRef<ModalsController, {}>(({}, ref) => {
  const modalContentRef = useRef<HTMLDivElement>(null);
  const [modals, setModals] = useState<Modal[]>([]);

  useImperativeHandle(ref, () => ({
    open(modal) {
      setModals(old => {
        const copy = [...old];
        copy.push(modal);
        return copy;
      });
    },
    close(key) {
      setModals(old => {
        return old.filter(m => m.key !== key);
      });
    },
  }));

  useEffect(() => {
    const clickHandler = ({ target }: MouseEvent) => {
      if (modalContentRef.current && modalContentRef.current.contains(target as Node)) return;
      setModals(old => {
        const copy = [...old];
        copy.splice(-1);
        return copy;
      });
    };
    document.addEventListener('mousedown', clickHandler);

    const keyHandler = ({ keyCode }: KeyboardEvent) => {
      if (keyCode !== 27) return;
      setModals(old => {
        const copy = [...old];
        copy.splice(-1);
        return copy;
      });
    };
    document.addEventListener('keydown', keyHandler);

    return () => {
      document.removeEventListener('mousedown', clickHandler);
      document.removeEventListener('keydown', keyHandler);
    };
  }, []);

  return (
    <>
      <Transition
        className="fixed w-screen h-screen inset-0 bg-slate-900 bg-opacity-30 z-50 transition-opacity"
        show={modals.length > 0}
        enter="transition ease-out duration-200"
        enterStart="opacity-0"
        enterEnd="opacity-100"
        leave="transition ease-out duration-100"
        leaveStart="opacity-100"
        leaveEnd="opacity-0"
        appear={true}
      />
      {modals.map((modal, k) => (
        <Transition
          key={`modal.${modal.key}`}
          id={`modal.${modal.key}`}
          className={`fixed w-screen h-screen inset-0 overflow-hidden flex items-start top-20 mb-4 justify-center transform px-4 sm:px-6 ${
            k < modals.length - 1 ? 'z-40' : 'z-60'
          }`}
          role="dialog"
          aria-modal="true"
          show={true}
          enter="transition ease-in-out duration-200"
          enterStart="opacity-0 translate-y-4"
          enterEnd="opacity-100 translate-y-0"
          leave="transition ease-in-out duration-200"
          leaveStart="opacity-100 translate-y-0"
          leaveEnd="opacity-0 translate-y-4"
          appear={true}
        >
          <div
            ref={k === modals.length - 1 ? modalContentRef : undefined}
            className="bg-white overflow-visible max-w-2xl w-max max-h-full rounded shadow-lg"
          >
            {modal.content}
          </div>
        </Transition>
      ))}
    </>
  );
});

export default ModalView;
