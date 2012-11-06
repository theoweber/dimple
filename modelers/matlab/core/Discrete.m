%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%   Copyright 2012 Analog Devices, Inc.
%
%   Licensed under the Apache License, Version 2.0 (the "License");
%   you may not use this file except in compliance with the License.
%   You may obtain a copy of the License at
%
%       http://www.apache.org/licenses/LICENSE-2.0
%
%   Unless required by applicable law or agreed to in writing, software
%   distributed under the License is distributed on an "AS IS" BASIS,
%   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
%   See the License for the specific language governing permissions and
%   limitations under the License.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

classdef Discrete < DiscreteVariableBase
    
    properties
        Input;
        Belief;
        Value;
    end
    methods
        function obj = Discrete(domain,varargin)
            obj@DiscreteVariableBase(domain,varargin{:});
        end
        
        function x = get.Input(obj)
            x = obj.getInput();
        end
        function set.Input(obj,input)
            obj.setInput(input);
        end
        function x = get.Belief(obj)
            x = obj.getBelief();
        end
        function x = get.Value(obj)
            x = obj.getValue();
        end
    end
    
    methods (Access = protected)
        function retval = createObject(obj,vectorObject,indices)
            retval = Discrete(obj.Domain,'existing',vectorObject,indices);
        end
        
        
    end
    
end
